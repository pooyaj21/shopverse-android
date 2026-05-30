package com.shopverse.core.service.api

import com.shopverse.core.service.api.dto.SubmitOrderEnvelopeDto
import com.shopverse.core.service.api.dto.SubmitOrderItemDto
import com.shopverse.core.service.api.dto.SubmitOrderRequestDto
import com.shopverse.core.service.api.dto.SubmittedOrderDto
import com.shopverse.core.service.supabase.SupabaseClient
import com.shopverse.core.shared.AppResult
import com.shopverse.core.shared.mapIfSuccess
import kotlinx.serialization.json.Json

interface OrderService {
    suspend fun submit(
        bearer: String,
        items: List<SubmitOrderItemDto>,
    ): AppResult<SubmittedOrderDto>
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
}
