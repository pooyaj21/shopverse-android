package com.shopverse.core.domain.order

import com.shopverse.core.data.order.OrderRepository
import com.shopverse.core.domain.UseCase
import com.shopverse.core.model.OrderDetail
import com.shopverse.core.shared.AppResult

class GetOrderUseCase(
    private val orderRepository: OrderRepository,
) : UseCase {
    suspend operator fun invoke(orderId: String): AppResult<OrderDetail> =
        orderRepository.getOrder(orderId = orderId)
}
