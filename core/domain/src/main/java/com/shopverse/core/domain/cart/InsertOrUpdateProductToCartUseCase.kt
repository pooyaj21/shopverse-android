package com.shopverse.core.domain.cart

import com.shopverse.core.data.cart.CartRepository
import com.shopverse.core.domain.UseCase
import com.shopverse.core.model.LocalCartItem

class InsertOrUpdateProductToCartUseCase(
    private val cartRepository: CartRepository,
) : UseCase {
    suspend operator fun invoke(item: LocalCartItem) = cartRepository.insertOrUpdate(item)
}
