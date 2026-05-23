package com.shopverse.android.presentation.screen.home

import com.shopverse.core.model.Product

data class HomeUiModel(
    val items: List<Product>,
    val cartIds: Set<String>,
    val hasMore: Boolean,
    val isAppending: Boolean,
)
