package com.shopverse.android.presentation.screen.productDetail

import androidx.lifecycle.viewModelScope
import com.shopverse.android.core.cart.CartManager
import com.shopverse.android.presentation.architecture.Alert
import com.shopverse.android.presentation.architecture.BaseViewModelState
import com.shopverse.core.domain.product.GetProductUseCase
import com.shopverse.core.model.Product
import com.shopverse.core.shared.AppResult
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ProductDetailViewModel(
    private val productId: String,
    private val getProductUseCase: GetProductUseCase,
    private val cartManager: CartManager,
) : BaseViewModelState<ProductDetailUiModel, Unit>() {

    private var product: Product? = null

    init {
        loadProduct()
        cartManager.idsFlow
            .onEach { ids ->
                val loaded = product ?: return@onEach
                setSuccessState(
                    ProductDetailUiModel(product = loaded, isInCart = loaded.id in ids)
                )
            }
            .launchIn(viewModelScope)
    }

    fun loadProduct() {
        viewModelScope.launch {
            setLoadingState(onFrontOfContent = false)
            when (val result = getProductUseCase(productId = productId)) {
                is AppResult.Success -> {
                    val loaded = result.value
                    product = loaded
                    setSuccessState(
                        ProductDetailUiModel(
                            product = loaded,
                            isInCart = loaded.id in cartManager.idsFlow.value,
                        )
                    )
                }

                is AppResult.Error -> setErrorState(result)
            }
        }
    }

    fun addToCart() {
        viewModelScope.launch {
            val loaded = product ?: return@launch
            cartManager.add(loaded)
            sendAlert(Alert.Success("Added to cart."))
        }
    }
}
