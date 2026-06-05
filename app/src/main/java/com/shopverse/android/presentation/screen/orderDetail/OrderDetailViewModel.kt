package com.shopverse.android.presentation.screen.orderDetail

import androidx.lifecycle.viewModelScope
import com.shopverse.android.presentation.architecture.BaseViewModelState
import com.shopverse.core.domain.order.GetOrderUseCase
import com.shopverse.core.shared.AppResult
import kotlinx.coroutines.launch

class OrderDetailViewModel(
    private val orderId: String,
    private val getOrderUseCase: GetOrderUseCase,
) : BaseViewModelState<OrderDetailUiModel, Unit>() {

    init {
        loadOrder()
    }

    fun loadOrder() {
        viewModelScope.launch {
            setLoadingState(onFrontOfContent = false)
            when (val result = getOrderUseCase(orderId = orderId)) {
                is AppResult.Success -> setSuccessState(OrderDetailUiModel(order = result.value))
                is AppResult.Error -> setErrorState(result)
            }
        }
    }
}
