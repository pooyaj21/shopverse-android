package com.shopverse.core.model

data class PagedResult<T>(
    val items: List<T>,
    val offset: Int,
    val limit: Int,
    val total: Int,
) {
    val hasMore: Boolean get() = offset + items.size < total

    companion object {
        const val DEFAULT_PAGE_SIZE = 20
    }
}
