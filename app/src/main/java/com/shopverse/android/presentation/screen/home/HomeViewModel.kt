package com.shopverse.android.presentation.screen.home

import androidx.lifecycle.viewModelScope
import com.shopverse.android.presentation.architecture.BaseViewModelState
import com.shopverse.android.presentation.architecture.ViewState
import com.shopverse.core.domain.product.GetProductsUseCase
import com.shopverse.core.model.PagedResult
import com.shopverse.core.model.Product
import com.shopverse.core.shared.AppResult
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getProducts: GetProductsUseCase,
) : BaseViewModelState<HomeUiModel, Unit>(
    initialState = ViewState.Loading(onFrontOfContent = false)
) {

    private val pageSize = PagedResult.DEFAULT_PAGE_SIZE
    private val accumulated = mutableListOf<Product>()
    private var nextOffset = 0
    private var total = Int.MAX_VALUE
    private var loading = false

    init {
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
                        when {
                            accumulated.isEmpty() -> setEmptyState()
                            else -> setSuccessState(
                                HomeUiModel(
                                    items = accumulated.toList(),
                                    hasMore = nextOffset < total,
                                    isAppending = isAppending,
                                )
                            )
                        }
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
}
