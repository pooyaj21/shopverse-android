package com.shopverse.core.service.api

import com.shopverse.core.model.PagedResult
import com.shopverse.core.service.api.dto.ProductDto
import com.shopverse.core.service.supabase.SupabaseClient
import com.shopverse.core.shared.AppResult
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

interface ProductService {
    suspend fun list(
        limit: Int = PagedResult.DEFAULT_PAGE_SIZE,
        offset: Int = 0,
    ): AppResult<PagedResult<ProductDto>>
}

class ProductServiceImpl(
    private val client: SupabaseClient,
    private val json: Json,
) : ProductService {

    override suspend fun list(limit: Int, offset: Int): AppResult<PagedResult<ProductDto>> =
        client.get(
            path = "products",
            query = mapOf(
                "order" to "release_date.desc",
                "limit" to limit.toString(),
                "offset" to offset.toString(),
            ),
            extraHeaders = mapOf("Prefer" to "count=exact"),
        ) { body, headers ->
            val items = json.decodeFromString(ListSerializer(ProductDto.serializer()), body)
            val total = headers["Content-Range"]
                ?.substringAfter('/')
                ?.toIntOrNull()
                ?: (offset + items.size)
            PagedResult(items = items, offset = offset, limit = limit, total = total)
        }
}
