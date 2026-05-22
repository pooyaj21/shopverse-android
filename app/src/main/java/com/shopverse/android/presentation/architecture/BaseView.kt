package com.shopverse.android.presentation.architecture

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.core.view.isVisible
import com.shopverse.android.core.color.AppColorProvider
import com.shopverse.android.core.extension.dp
import com.shopverse.android.core.extension.setPadding
import com.shopverse.android.core.extension.setTextColor
import com.shopverse.android.core.extension.setTypography
import com.shopverse.android.core.layout.AppLayout
import com.shopverse.android.core.typography.Typography
import com.shopverse.android.core.ui.AppVerticalLinearLayout
import com.shopverse.android.presentation.component.AppTextView

@SuppressLint("ViewConstructor")
abstract class BaseView(context: Context) : FrameLayout(context) {

    protected abstract val title: String

    private val titleView = AppTextView(context).apply {
        setTypography(Typography.B24)
        setTextColor(AppColorProvider.primaryMain)
        setPadding(horizontal = 16.dp, vertical = 12.dp)
    }

    protected lateinit var contentView: View
        private set

    protected fun setContent(view: View) {
        contentView = view
        titleView.text = title
        titleView.isVisible = title.isNotEmpty()
        val wrapper = AppVerticalLinearLayout(context).apply {
            addView(titleView, AppLayout.Linear.defaultParams())
            addView(view, AppLayout.Linear.justOccupyScreen())
        }
        addView(wrapper, AppLayout.Frame.fullScreen())
        onContentAttached()
    }

    protected open fun onContentAttached() = Unit

    @SuppressLint("ViewConstructor")
    abstract class State<Model>(
        context: Context,
        final override val onRetryClickListener: OnClickListener
    ) : BaseView(context), ViewStateRenderer<Model> {

        private val progressView = ProgressBar(context).apply {
            isVisible = false
        }

        private val messageView = AppTextView(context).apply {
            isVisible = false
            gravity = Gravity.CENTER
            setTypography(Typography.R16)
            setTextColor(AppColorProvider.gray)
            setPadding(horizontal = 24.dp, vertical = 24.dp)
            setOnClickListener { onRetryClickListener.onClick(it) }
        }

        protected open val emptyMessage: String = "Nothing here yet."

        protected open fun localErrorMessage(message: String?, cause: Throwable?): String =
            "Network problem. Tap to retry."

        protected open fun remoteErrorMessage(httpCode: Int, message: String?): String =
            "Something went wrong ($httpCode). Tap to retry."

        protected abstract fun renderSuccess(model: Model)

        protected open fun onContentReset() = Unit

        override fun onContentAttached() {
            addView(progressView, AppLayout.Frame.wrapContent().gravity(Gravity.CENTER))
            addView(messageView, AppLayout.Frame.defaultParams().gravity(Gravity.CENTER))
        }

        override fun onLoading(onFrontOfContent: Boolean) {
            progressView.isVisible = true
            messageView.isVisible = false
            contentView.isVisible = onFrontOfContent
        }

        override fun onSuccess(model: Model) {
            progressView.isVisible = false
            messageView.isVisible = false
            contentView.isVisible = true
            renderSuccess(model)
        }

        override fun onEmpty() {
            showMessage(emptyMessage)
        }

        override fun onLocalError(message: String?, cause: Throwable?) {
            showMessage(localErrorMessage(message, cause))
        }

        override fun onRemoteError(httpCode: Int, message: String?) {
            showMessage(remoteErrorMessage(httpCode, message))
        }

        private fun showMessage(text: String) {
            progressView.isVisible = false
            contentView.isVisible = false
            onContentReset()
            messageView.text = text
            messageView.isVisible = true
        }
    }
}
