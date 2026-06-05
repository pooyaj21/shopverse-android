package com.shopverse.core.domain.product

import com.shopverse.core.data.product.ProductRepository
import com.shopverse.core.domain.UseCase
import com.shopverse.core.model.Product
import com.shopverse.core.shared.AppResult

class GetProductUseCase(
    private val productRepository: ProductRepository,
) : UseCase {
    suspend operator fun invoke(productId: String): AppResult<Product> =
        productRepository.getProduct(productId = productId)
}
