package com.shopverse.android.presentation.architecture

import android.content.Context
import android.content.res.ColorStateList
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.shopverse.android.R
import com.shopverse.android.core.Screen
import com.shopverse.android.core.color.AppColorProvider
import com.shopverse.android.core.extension.dp
import com.shopverse.android.core.extension.setBackgroundColor
import com.shopverse.android.core.extension.setDismissOnHiddenBehavior
import com.shopverse.android.core.extension.setPadding
import com.shopverse.android.core.extension.setTextColor
import com.shopverse.android.core.extension.setTypography
import com.shopverse.android.core.layout.AppLayout
import com.shopverse.android.core.typography.Typography
import com.shopverse.android.core.ui.AppVerticalLinearLayout
import com.shopverse.android.presentation.component.AppCornerView
import com.shopverse.android.presentation.component.AppTextView

abstract class BaseBottomSheetDialog(context: Context) : BottomSheetDialog(context) {
    private val dialogCorner = 16.dp.toFloat()
    protected val fullScreen: Int get() = (Screen.size.height * 0.9).toInt()
    protected open val isExpandedByDefault: Boolean = true

    protected open val title: String = ""

    protected val contentView = AppVerticalLinearLayout(context).apply {
        setBackgroundColor(AppColorProvider.white)
    }

    private val titleView = AppTextView(context).apply {
        gravity = Gravity.CENTER
        setTypography(Typography.B24)
        setTextColor(AppColorProvider.primaryMain)
    }

    private val closeButton = ImageView(context).apply {
        setImageResource(R.drawable.ic_close)
        imageTintList = ColorStateList.valueOf(AppColorProvider.gray.value(context))
        setPadding(horizontal = 8.dp, vertical = 8.dp)
        setOnClickListener { dismiss() }
    }

    private val headerView = FrameLayout(context).apply {
        setPadding(horizontal = 8.dp, vertical = 4.dp)
        addView(
            titleView,
            AppLayout.Frame.defaultParams().gravity(Gravity.CENTER)
        )
        addView(
            closeButton,
            AppLayout.Frame.wrapContent().gravity(Gravity.END or Gravity.CENTER_VERTICAL)
        )
    }

    protected val rootView = AppCornerView(context, dialogCorner, dialogCorner, 0F, 0F).apply {
        addView(contentView, AppLayout.Frame.fullScreen())
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        ViewCompat.setOnApplyWindowInsetsListener(rootView) { _, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            contentView.updatePadding(bottom = systemBars.bottom)
            WindowInsetsCompat.CONSUMED
        }
        findViewById<View>(com.google.android.material.R.id.container)?.apply {
            fitsSystemWindows = false
        }

        findViewById<View>(com.google.android.material.R.id.coordinator)?.fitsSystemWindows = false
    }


    protected open fun setupView(view: View, height: Int) {
        val dialogHeight = if (height == AppLayout.MATCH || height > fullScreen) fullScreen
        else height
        titleView.text = title
        titleView.isVisible = title.isNotEmpty()
        contentView.apply {
            addView(headerView, AppLayout.Linear.defaultParams())
            addView(view, AppLayout.Linear.justOccupyScreen())
        }
        if (isExpandedByDefault) setDismissOnHiddenBehavior()
        super.setContentView(rootView, AppLayout.Frame.get(AppLayout.MATCH, dialogHeight))
    }

    final override fun setContentView(view: View) {
        throw IllegalStateException("Call setupView instead")
    }

    final override fun setContentView(view: View, params: ViewGroup.LayoutParams?) {
        throw IllegalStateException("Call setupView instead")
    }

}