package com.alekseykostyunin.enot.domain.usecase.orders

import com.alekseykostyunin.enot.domain.entities.Order
import com.alekseykostyunin.enot.domain.repository.OrdersRepository
import kotlinx.coroutines.flow.Flow

class AddHistoryStepUseCase(private val ordersRepository: OrdersRepository) {
    fun invoke(order: Order, descStep: String): Flow<Order> = ordersRepository.addHistoryStep(order, descStep)
}