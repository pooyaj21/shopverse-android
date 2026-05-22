package com.shopverse.android.presentation.architecture

import com.shopverse.core.shared.AppResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class BaseViewModelState<Model, ViewEffect>(
    initialState: ViewState<Model> = ViewState.Loading(false)
) : BaseViewModel<ViewEffect>() {

    // UiStates

    private val mutableViewStateFlow = MutableStateFlow(initialState)
    val viewStateFlow = mutableViewStateFlow.asStateFlow()

    protected val state: ViewState<Model>
        get() = mutableViewStateFlow.value

    protected suspend fun setState(state: ViewState<Model>) = mutableViewStateFlow.emit(state)

    protected suspend fun setLoadingState(onFrontOfContent: Boolean) = mutableViewStateFlow.emit(
        ViewState.Loading(onFrontOfContent)
    )
    protected suspend fun setEmptyState() = mutableViewStateFlow.emit(ViewState.Empty)

    protected suspend fun setSuccessState(model: Model) = mutableViewStateFlow.emit(
        ViewState.Success(
            model
        )
    )

    protected suspend fun setLocalErrorState(
        message: String? = null,
        cause: Throwable?
    ) = mutableViewStateFlow.emit(ViewState.Error.Local(message, cause))

    protected suspend fun setRemoteErrorState(httpCode: Int, message: String?) =
        mutableViewStateFlow.emit(ViewState.Error.Remote(httpCode, message))

    protected suspend fun setErrorState(result: AppResult.Error) {
        val errorState = when (result) {
            is AppResult.Error.Local -> ViewState.Error.Local(message = null, cause = result.cause)
            is AppResult.Error.Remote -> ViewState.Error.Remote(
                httpCode = result.httpCode,
                message = result.message
            )
        }
        mutableViewStateFlow.emit(errorState)
    }
}