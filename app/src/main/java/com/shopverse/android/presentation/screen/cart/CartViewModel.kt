package com.shopverse.android.presentation.screen.cart

import androidx.lifecycle.viewModelScope
import com.shopverse.android.core.cart.CartManager
import com.shopverse.android.presentation.architecture.BaseViewModelState
import com.shopverse.android.presentation.architecture.ViewState
import com.shopverse.core.model.LocalCartItem
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class CartViewModel(
    private val cartManager: CartManager,
) : BaseViewModelState<CartUiModel, Unit>(
    initialState = ViewState.Loading(onFrontOfContent = false)
) {

    init {
        cartManager.itemsFlow
            .onEach { items -> render(items) }
            .launchIn(viewModelScope)
    }

    fun removeFromCart(item: LocalCartItem) {
        viewModelScope.launch { cartManager.remove(item.id) }
    }

    private suspend fun render(items: List<LocalCartItem>) {
        if (items.isEmpty()) setEmptyState()
        else setSuccessState(CartUiModel(items = items))
    }
}
