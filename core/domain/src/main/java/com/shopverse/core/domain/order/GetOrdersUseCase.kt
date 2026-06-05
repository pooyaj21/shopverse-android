package com.shopverse.core.domain.order

import com.shopverse.core.data.order.OrderRepository
import com.shopverse.core.domain.UseCase
import com.shopverse.core.model.OrderSummary
import com.shopverse.core.model.PagedResult
import com.shopverse.core.shared.AppResult

class GetOrdersUseCase(
    private val orderRepository: OrderRepository,
) : UseCase {
    suspend operator fun invoke(
        limit: Int = PagedResult.DEFAULT_PAGE_SIZE,
        offset: Int = 0,
    ): AppResult<PagedResult<OrderSummary>> =
        orderRepository.getOrders(limit = limit, offset = offset)
}
