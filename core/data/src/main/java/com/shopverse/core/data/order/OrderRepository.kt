package com.shopverse.core.data.order

import com.shopverse.core.data.auth.AuthRepository
import com.shopverse.core.model.LocalCartItem
import com.shopverse.core.model.OrderDetail
import com.shopverse.core.model.OrderSummary
import com.shopverse.core.model.PagedResult
import com.shopverse.core.service.api.OrderService
import com.shopverse.core.service.api.dto.SubmitOrderItemDto
import com.shopverse.core.service.api.dto.toDomain
import com.shopverse.core.shared.AppResult
import com.shopverse.core.shared.mapIfSuccess

interface OrderRepository {
    suspend fun submit(items: List<LocalCartItem>): AppResult<String>

    suspend fun getOrders(
        limit: Int = PagedResult.DEFAULT_PAGE_SIZE,
        offset: Int = 0,
    ): AppResult<PagedResult<OrderSummary>>

    suspend fun getOrder(orderId: String): AppResult<OrderDetail>
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

    override suspend fun getOrders(
        limit: Int,
        offset: Int,
    ): AppResult<PagedResult<OrderSummary>> {
        val bearer = authRepository.getAccessToken()
            ?: return AppResult.Error.Remote(httpCode = 401, message = "unauthenticated", cause = null)
        return orderService.list(bearer = bearer, limit = limit, offset = offset)
            .mapIfSuccess { dtoPage ->
                PagedResult(
                    items = dtoPage.items.map { it.toDomain() },
                    offset = dtoPage.offset,
                    limit = dtoPage.limit,
                    total = dtoPage.total,
                )
            }
    }

    override suspend fun getOrder(orderId: String): AppResult<OrderDetail> {
        val bearer = authRepository.getAccessToken()
            ?: return AppResult.Error.Remote(httpCode = 401, message = "unauthenticated", cause = null)
        return orderService.getById(bearer = bearer, id = orderId)
            .mapIfSuccess { it.toDomain() }
    }
}
