package com.shopverse.core.domain.order

import com.shopverse.core.data.order.OrderRepository
import com.shopverse.core.domain.UseCase
import com.shopverse.core.model.LocalCartItem
import com.shopverse.core.shared.AppResult

class SubmitOrderUseCase(
    private val orderRepository: OrderRepository,
) : UseCase {
    suspend operator fun invoke(items: List<LocalCartItem>): AppResult<String> =
        orderRepository.submit(items = items)
}
