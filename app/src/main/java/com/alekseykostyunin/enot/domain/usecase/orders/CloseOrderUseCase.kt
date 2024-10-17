package com.alekseykostyunin.enot.domain.usecase.orders

import com.alekseykostyunin.enot.domain.entities.Order
import com.alekseykostyunin.enot.domain.repository.OrdersRepository
import kotlinx.coroutines.flow.Flow

class CloseOrderUseCase(private val ordersRepository: OrdersRepository) {
    fun invoke(order: Order): Flow<Order> = ordersRepository.closeOrder(order)
}