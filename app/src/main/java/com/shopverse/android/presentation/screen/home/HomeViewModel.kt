package com.shopverse.android.presentation.screen.home

import androidx.lifecycle.viewModelScope
import com.shopverse.android.core.cart.CartManager
import com.shopverse.android.presentation.architecture.BaseViewModelState
import com.shopverse.android.presentation.architecture.ViewState
import com.shopverse.core.domain.product.GetProductsUseCase
import com.shopverse.core.model.PagedResult
import com.shopverse.core.model.Product
import com.shopverse.core.shared.AppResult
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getProducts: GetProductsUseCase,
    private val cartManager: CartManager,
) : BaseViewModelState<HomeUiModel, Unit>() {

    private val pageSize = PagedResult.DEFAULT_PAGE_SIZE
    private val accumulated = mutableListOf<Product>()
    private var nextOffset = 0
    private var total = Int.MAX_VALUE
    private var loading = false

    init {
        cartManager.idsFlow
            .onEach { ids -> onCartIdsChanged(ids) }
            .launchIn(viewModelScope)
        refresh()
    }

    fun refresh() {
        accumulated.clear()
        nextOffset = 0
        total = Int.MAX_VALUE
        loading = false
        loadMore()
    }

    fun loadMore() {
        if (loading || nextOffset >= total) return
        loading = true
        val isAppending = accumulated.isNotEmpty()

        viewModelScope.launch {
            try {
                if (!isAppending) setLoadingState(onFrontOfContent = false)
                when (val result = getProducts(limit = pageSize, offset = nextOffset)) {
                    is AppResult.Success -> {
                        val page = result.value
                        accumulated.addAll(page.items)
                        nextOffset = page.offset + page.items.size
                        total = page.total
                        emitSuccessOrEmpty(isAppending = isAppending)
                    }
                    is AppResult.Error -> if (!isAppending) setErrorState(result)
                    // Append failures are silently dropped for now — the next
                    // scroll-near-end will re-trigger. Surface via effect later.
                }
            } finally {
                loading = false
            }
        }
    }

    fun addToCart(product: Product) {
        viewModelScope.launch { cartManager.add(product) }
    }

    private suspend fun onCartIdsChanged(cartIds: Set<String>) {
        val current = state
        if (current is ViewState.Success && current.model.cartIds != cartIds) {
            setSuccessState(current.model.copy(cartIds = cartIds))
        }
    }

    private suspend fun emitSuccessOrEmpty(isAppending: Boolean) {
        if (accumulated.isEmpty()) {
            setEmptyState()
            return
        }
        setSuccessState(
            HomeUiModel(
                items = accumulated.toList(),
                cartIds = cartManager.idsFlow.value,
                hasMore = nextOffset < total,
                isAppending = isAppending,
            )
        )
    }
}
