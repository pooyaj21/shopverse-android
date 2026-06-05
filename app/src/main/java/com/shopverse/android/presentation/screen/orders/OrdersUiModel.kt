package com.shopverse.android.presentation.screen.orders

import com.shopverse.core.model.OrderSummary

data class OrdersUiModel(
    val items: List<OrderSummary>,
    val hasMore: Boolean,
    val isAppending: Boolean,
)
