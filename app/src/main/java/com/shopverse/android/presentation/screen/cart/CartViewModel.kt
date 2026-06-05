package com.shopverse.android.presentation.screen.cart

import androidx.lifecycle.viewModelScope
import com.shopverse.android.core.cart.CartManager
import com.shopverse.android.presentation.architecture.BaseViewModelState
import com.shopverse.core.domain.order.SubmitOrderUseCase
import com.shopverse.core.model.LocalCartItem
import com.shopverse.core.shared.AppResult
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class CartViewModel(
    private val cartManager: CartManager,
    private val submitOrderUseCase: SubmitOrderUseCase,
) : BaseViewModelState<CartUiModel, CartEffect>() {

    private var isPlacingOrder: Boolean = false

    init {
        cartManager.itemsFlow
            .onEach { items -> render(items) }
            .launchIn(viewModelScope)
    }

    fun removeFromCart(item: LocalCartItem) {
        viewModelScope.launch { cartManager.remove(item.id) }
    }

    fun placeOrder() {
        if (isPlacingOrder) return
        val items = cartManager.itemsFlow.value
        if (items.isEmpty()) {
            viewModelScope.launch { sendEffect(CartEffect.ShowMessage("Your cart is empty.")) }
            return
        }
        isPlacingOrder = true
        viewModelScope.launch {
            setLoadingState(false)
            when (val result = submitOrderUseCase(items)) {
                is AppResult.Success -> {
                    cartManager.clear()
                    sendEffect(CartEffect.ShowMessage("Order placed!"))
                    sendEffect(CartEffect.OrderPlaced(orderId = result.value))
                }

                is AppResult.Error.Local -> {
                    render(cartManager.itemsFlow.value)
                    sendEffect(
                        CartEffect.ShowMessage("Network problem. Please try again.")
                    )
                }

                is AppResult.Error.Remote -> {
                    render(cartManager.itemsFlow.value)
                    sendEffect(
                        CartEffect.ShowMessage(prettyRemoteError(result.httpCode, result.message))
                    )
                }
            }
            isPlacingOrder = false
        }
    }

    private suspend fun render(items: List<LocalCartItem>) {
        if (items.isEmpty()) setEmptyState()
        else setSuccessState(CartUiModel(items = items))
    }

    private fun prettyRemoteError(httpCode: Int, message: String?): String = when {
        httpCode == 401 -> "Please log in to place an order."
        message.isNullOrBlank() -> "Something went wrong ($httpCode)."
        message.contains("insufficient_stock", ignoreCase = true) -> "Some items are out of stock."
        message.contains(
            "product_not_found",
            ignoreCase = true
        ) -> "A product is no longer available."

        else -> "Something went wrong ($httpCode)."
    }
}
