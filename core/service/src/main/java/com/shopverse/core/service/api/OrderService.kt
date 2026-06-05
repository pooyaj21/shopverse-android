package com.shopverse.core.service.api

import com.shopverse.core.model.PagedResult
import com.shopverse.core.service.api.dto.OrderSummaryDto
import com.shopverse.core.service.api.dto.SubmitOrderEnvelopeDto
import com.shopverse.core.service.api.dto.SubmitOrderItemDto
import com.shopverse.core.service.api.dto.SubmitOrderRequestDto
import com.shopverse.core.service.api.dto.SubmittedOrderDto
import com.shopverse.core.service.supabase.SupabaseClient
import com.shopverse.core.shared.AppResult
import com.shopverse.core.shared.mapIfSuccess
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

interface OrderService {
    suspend fun submit(
        bearer: String,
        items: List<SubmitOrderItemDto>,
    ): AppResult<SubmittedOrderDto>

    suspend fun list(
        bearer: String,
        limit: Int = PagedResult.DEFAULT_PAGE_SIZE,
        offset: Int = 0,
    ): AppResult<PagedResult<OrderSummaryDto>>
}

class OrderServiceImpl(
    private val client: SupabaseClient,
    private val json: Json,
) : OrderService {

    override suspend fun submit(
        bearer: String,
        items: List<SubmitOrderItemDto>,
    ): AppResult<SubmittedOrderDto> {
        val payload = json.encodeToString(
            SubmitOrderRequestDto.serializer(),
            SubmitOrderRequestDto(items = items),
        )
        return client.functionsPost(
            path = "submit-order",
            body = payload,
            bearer = bearer,
        ) { body, _ -> json.decodeFromString(SubmitOrderEnvelopeDto.serializer(), body) }
            .mapIfSuccess { envelope ->
                checkNotNull(envelope.data) { "submit-order returned no data: ${envelope.meta?.msg}" }
            }
    }

    override suspend fun list(
        bearer: String,
        limit: Int,
        offset: Int,
    ): AppResult<PagedResult<OrderSummaryDto>> =
        client.get(
            path = "orders",
            query = mapOf(
                "select" to "id,placed_at,total,original_total,currency",
                "order" to "placed_at.desc",
                "limit" to limit.toString(),
                "offset" to offset.toString(),
            ),
            extraHeaders = mapOf("Prefer" to "count=exact"),
            bearer = bearer,
        ) { body, headers ->
            val items = json.decodeFromString(ListSerializer(OrderSummaryDto.serializer()), body)
            val total = headers["Content-Range"]
                ?.substringAfter('/')
                ?.toIntOrNull()
                ?: (offset + items.size)
            PagedResult(items = items, offset = offset, limit = limit, total = total)
        }
}
