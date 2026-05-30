package com.shopverse.android.presentation.architecture

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.widget.FrameLayout
import androidx.annotation.CallSuper
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.google.android.material.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.shopverse.android.core.Screen
import com.shopverse.android.core.color.AppColorProvider
import com.shopverse.android.core.extension.dp
import com.shopverse.android.core.extension.isRTL
import com.shopverse.android.core.extension.setBackgroundColor
import com.shopverse.android.core.layout.AppLayout
import com.shopverse.android.presentation.component.AppCornerView
import com.shopverse.android.presentation.ui.Source

abstract class BaseBottomSheetDialogFragment<V : View> : BottomSheetDialogFragment() {

    protected abstract val currentSource: Source
    open val dialogCorner: Float = 16.dp.toFloat()
    private var onDismissListener: OnDismissListener? = null

    // RootView
    protected var rootView: V? = null

    protected val fullScreen: Int get() = (Screen.size.height * 0.9).toInt()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            onDismissListener =
                (parentFragment as? OnDismissListener) ?: (requireActivity() as? OnDismissListener)
        } catch (e: ClassCastException) {
            throw ClassCastException("implement OnDismissListener interface In your fragment")
        }
    }

    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Come up Keyboard
        return AppCornerView(requireContext(), dialogCorner, dialogCorner, 0F, 0F).apply {
            elevation = 6.dp.toFloat()
            val view = onCreateContainerView(inflater, container, savedInstanceState)
            if (view.background == null) {
                view.setBackgroundColor(AppColorProvider.white)
            }
            this@BaseBottomSheetDialogFragment.rootView = view
            addView(
                view,
                AppLayout.Frame.get(AppLayout.MATCH, getDialogHeightAdjusted())
            )
        }

    }

    abstract fun onCreateContainerView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): V

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*
        * It seems that localization doesn't work properly on dialogs if you selected the language
        * setting in Rtl and then closed the app and reopened it.
        * We therefore have to manually set the layout direction!
        * */
        view.layoutDirection =
            if (requireContext().isRTL()) View.LAYOUT_DIRECTION_RTL else View.LAYOUT_DIRECTION_LTR
    }

    abstract fun getDialogHeight(): Int

    private fun getDialogHeightAdjusted(): Int {
        val dialogHeight = getDialogHeight()
        return if (dialogHeight == AppLayout.MATCH || dialogHeight > fullScreen) fullScreen
        else dialogHeight
    }

    override fun getTheme(): Int = com.shopverse.android.R.style.AppBottomSheetDialogTheme

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        object : BottomSheetDialog(
            requireContext(),
            com.shopverse.android.R.style.AppBottomSheetDialogTheme
        ) {
            override fun onAttachedToWindow() {
                super.onAttachedToWindow()
                rootView?.let { view ->
                    ViewCompat.setOnApplyWindowInsetsListener(view) { _, insets ->
                        val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                        view.updatePadding(bottom = systemBars.bottom)
                        WindowInsetsCompat.CONSUMED
                    }
                }
                findViewById<View>(R.id.container)?.apply {
                    fitsSystemWindows = false
                }

                findViewById<View>(R.id.coordinator)?.fitsSystemWindows =
                    false
            }
        }

    protected open fun onDestroyRootView() {
        // Implement if needed
    }

    @CallSuper
    override fun onDismiss(dialog: DialogInterface) {
        onDismissListener?.onDialogFragmentDismissed()
        super.onDismiss(dialog)
    }

    //****************************************
    //              Helper functions         *
    //****************************************/

    fun Dialog.handleBackPress(predicate: () -> Boolean) {
        setOnKeyListener { _: DialogInterface, keyCode: Int, keyEvent: KeyEvent ->
            if (keyCode == KeyEvent.KEYCODE_BACK && keyEvent.action == KeyEvent.ACTION_UP) {
                return@setOnKeyListener predicate.invoke()
            }
            return@setOnKeyListener false
        }
    }


    fun Dialog.addExpandedBehavior(
        scrollViewPredicate: (() -> Boolean)? = null,
    ) {
        setOnShowListener {
            val bottomSheet = findViewById<View>(
                R.id.design_bottom_sheet
            ) as? FrameLayout
            val behavior = BottomSheetBehavior.from(bottomSheet!!)
            // Setup Expanded by default
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            val layoutParams = bottomSheet.layoutParams
            behavior.peekHeight = 0
            val windowHeight: Int = getDialogHeightAdjusted()
            if (layoutParams != null) {
                layoutParams.height = windowHeight
            }
            bottomSheet.layoutParams = layoutParams
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            /*
            * When ScrollView has scope to scroll, bottom sheet will remain expanded.
            * But if ScrollView has no scroll to up direction, than drag from up, will revert
            * normal behaviour.
            * */
            behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                        behavior.state = BottomSheetBehavior.STATE_HIDDEN
                    }
                    if (newState == BottomSheetBehavior.STATE_DRAGGING && scrollViewPredicate?.invoke() != false) {
                        behavior.state = BottomSheetBehavior.STATE_EXPANDED
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    // Nothing
                }
            })
        }
    }

    fun Dialog.addDismissOnHiddenBehavior() {
        setOnShowListener {
            val bottomSheet = findViewById<View>(
                R.id.design_bottom_sheet
            ) as? FrameLayout
            val behavior = BottomSheetBehavior.from(bottomSheet!!)
            // Setup Expanded by default
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            // Hide Dialog if collapsed, Then Dismiss dialog if dialog hided.
            behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                        behavior.state = BottomSheetBehavior.STATE_HIDDEN
                    }
                    if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                        dismiss()
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    // Nothing
                }
            })
        }
    }

    fun updateDialogHeight() {
        val bottomSheet = dialog?.findViewById<FrameLayout>(
            R.id.design_bottom_sheet
        )
        bottomSheet?.let {
            val behavior = BottomSheetBehavior.from(it)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            val layoutParams = it.layoutParams
            layoutParams.height = getDialogHeightAdjusted()
            it.layoutParams = layoutParams
            it.requestLayout()
        }
        rootView?.let {
            val layoutParams = it.layoutParams
            layoutParams.height = getDialogHeightAdjusted()
            it.layoutParams = layoutParams
            it.requestLayout()
        }
    }

    interface OnDismissListener {
        fun onDialogFragmentDismissed()
    }
}