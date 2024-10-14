package com.alekseykostyunin.enot.domain.usecase.orders

import com.alekseykostyunin.enot.domain.entities.Order
import com.alekseykostyunin.enot.domain.repository.OrdersRepository

class AllOrdersUseCase(private val ordersRepository: OrdersRepository) {

    fun getAllOrders(): List<Order> = ordersRepository.getAllOrders()
}