package com.shopverse.android.presentation.screen.productDetail

import androidx.lifecycle.viewModelScope
import com.shopverse.android.core.cart.CartManager
import com.shopverse.android.presentation.architecture.Alert
import com.shopverse.android.presentation.architecture.BaseViewModelState
import com.shopverse.core.model.Product
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ProductDetailViewModel(
    private val product: Product,
    private val cartManager: CartManager,
) : BaseViewModelState<ProductDetailUiModel, Unit>() {

    init {
        cartManager.idsFlow
            .onEach { ids ->
                setSuccessState(
                    ProductDetailUiModel(product = product, isInCart = product.id in ids)
                )
            }
            .launchIn(viewModelScope)
    }

    fun addToCart() {
        viewModelScope.launch {
            cartManager.add(product)
            sendAlert(Alert.Success("Added to cart."))
        }
    }
}
