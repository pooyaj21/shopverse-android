package com.shopverse.android.presentation.architecture

import android.view.View

interface ViewStateRenderer<Model> {
    val onRetryClickListener: View.OnClickListener
    fun onLoading(onFrontOfContent: Boolean)
    fun onLocalError(message: String?, cause: Throwable?)
    fun onRemoteError(httpCode: Int, message: String?)
    fun onSuccess(model: Model)
    fun onEmpty()
    fun render(viewState: ViewState<Model>) {
        when (viewState) {
            is ViewState.Loading -> onLoading(viewState.onFrontOfContent)
            is ViewState.Success -> onSuccess(viewState.model)
            is ViewState.Error.Local -> onLocalError(viewState.message, viewState.cause)
            is ViewState.Error.Remote -> onRemoteError(viewState.httpCode, viewState.message)
            is ViewState.Empty -> onEmpty()
        }
    }
}