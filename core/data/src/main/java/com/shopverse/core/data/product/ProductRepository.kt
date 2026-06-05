package com.shopverse.core.data.product

import com.shopverse.core.model.PagedResult
import com.shopverse.core.model.Product
import com.shopverse.core.service.api.ProductService
import com.shopverse.core.service.api.dto.toDomain
import com.shopverse.core.shared.AppResult
import com.shopverse.core.shared.mapIfSuccess

interface ProductRepository {
    suspend fun getProducts(
        limit: Int = PagedResult.DEFAULT_PAGE_SIZE,
        offset: Int = 0,
    ): AppResult<PagedResult<Product>>

    suspend fun getProduct(productId: String): AppResult<Product>
}

class ProductRepositoryImpl(private val productService: ProductService) : ProductRepository {

    override suspend fun getProducts(limit: Int, offset: Int): AppResult<PagedResult<Product>> =
        productService.list(limit = limit, offset = offset).mapIfSuccess { dtoPage ->
            PagedResult(
                items = dtoPage.items.map { it.toDomain() },
                offset = dtoPage.offset,
                limit = dtoPage.limit,
                total = dtoPage.total,
            )
        }

    override suspend fun getProduct(productId: String): AppResult<Product> =
        productService.getById(id = productId).mapIfSuccess { it.toDomain() }
}
