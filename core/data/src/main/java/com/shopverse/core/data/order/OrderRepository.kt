package com.shopverse.core.data.order

import com.shopverse.core.data.auth.AuthRepository
import com.shopverse.core.model.LocalCartItem
import com.shopverse.core.service.api.OrderService
import com.shopverse.core.service.api.dto.SubmitOrderItemDto
import com.shopverse.core.shared.AppResult

interface OrderRepository {
    suspend fun submit(items: List<LocalCartItem>): AppResult<String>
}

class OrderRepositoryImpl(
    private val orderService: OrderService,
    private val authRepository: AuthRepository,
) : OrderRepository {

    override suspend fun submit(items: List<LocalCartItem>): AppResult<String> {
        val bearer = authRepository.getAccessToken()
            ?: return AppResult.Error.Remote(httpCode = 401, message = "unauthenticated", cause = null)
        return when (val result = orderService.submit(
            bearer = bearer,
            items = items.map {
                SubmitOrderItemDto(productId = it.id, quantity = it.count)
            },
        )) {
            is AppResult.Success -> AppResult.Success(result.value.id)
            is AppResult.Error.Local -> result
            is AppResult.Error.Remote -> result
        }
    }
}
