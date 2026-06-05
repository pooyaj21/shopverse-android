package com.shopverse.android.presentation.screen.orders

import androidx.lifecycle.viewModelScope
import com.shopverse.android.presentation.architecture.BaseViewModelState
import com.shopverse.android.presentation.architecture.ViewState
import com.shopverse.core.domain.order.GetOrdersUseCase
import com.shopverse.core.model.OrderSummary
import com.shopverse.core.model.PagedResult
import com.shopverse.core.shared.AppResult
import kotlinx.coroutines.launch

class OrdersViewModel(
    private val getOrders: GetOrdersUseCase,
) : BaseViewModelState<OrdersUiModel, Unit>(
    initialState = ViewState.Loading(onFrontOfContent = false)
) {

    private val pageSize = PagedResult.DEFAULT_PAGE_SIZE
    private val accumulated = mutableListOf<OrderSummary>()
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
                when (val result = getOrders(limit = pageSize, offset = nextOffset)) {
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

    private suspend fun emitSuccessOrEmpty(isAppending: Boolean) {
        if (accumulated.isEmpty()) {
            setEmptyState()
            return
        }
        setSuccessState(
            OrdersUiModel(
                items = accumulated.toList(),
                hasMore = nextOffset < total,
                isAppending = isAppending,
            )
        )
    }
}
