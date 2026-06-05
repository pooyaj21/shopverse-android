package com.shopverse.android.presentation.screen.orderDetail

import com.shopverse.core.model.OrderDetail

data class OrderDetailUiModel(
    val order: OrderDetail,
) {
    val savings: Double?
        get() = order.originalTotal
            ?.takeIf { it > order.total }
            ?.minus(order.total)

    // Matches the backend's DeeplinkBuilder format; the app will register
    // this scheme in the manifest once the deep link flow is set up.
    val deeplink: String get() = "$DEEPLINK_SCHEME://order/${order.id}"

    companion object {
        private const val DEEPLINK_SCHEME = "shopverse"
    }
}
