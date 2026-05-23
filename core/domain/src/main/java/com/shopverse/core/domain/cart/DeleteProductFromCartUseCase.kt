package com.shopverse.core.domain.cart

import com.shopverse.core.data.cart.CartRepository
import com.shopverse.core.domain.UseCase

class DeleteProductFromCartUseCase(
    private val cartRepository: CartRepository,
) : UseCase {
    suspend operator fun invoke(productId: String) = cartRepository.deleteByProductId(productId)
}
