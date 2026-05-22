package com.shopverse.core.domain.product

import com.shopverse.core.data.product.ProductRepository
import com.shopverse.core.domain.UseCase
import com.shopverse.core.model.PagedResult
import com.shopverse.core.model.Product
import com.shopverse.core.shared.AppResult

class GetProductsUseCase(
    private val productRepository: ProductRepository,
) : UseCase {
    suspend operator fun invoke(
        limit: Int = PagedResult.DEFAULT_PAGE_SIZE,
        offset: Int = 0,
    ): AppResult<PagedResult<Product>> =
        productRepository.getProducts(limit = limit, offset = offset)
}
