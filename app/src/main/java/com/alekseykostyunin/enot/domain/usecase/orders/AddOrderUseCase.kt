package com.alekseykostyunin.enot.domain.usecase.orders

import com.alekseykostyunin.enot.domain.entities.Order
import com.alekseykostyunin.enot.domain.repository.OrdersRepository

class AddOrderUseCase(private val ordersRepository: OrdersRepository) {

    fun addOrder(order: Order){
        ordersRepository.addOrder(order)
    }
}