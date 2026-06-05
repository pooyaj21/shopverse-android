package com.shopverse.android.presentation.screen.cart

sealed class CartEffect {
    data class OrderPlaced(val orderId: String) : CartEffect()
}