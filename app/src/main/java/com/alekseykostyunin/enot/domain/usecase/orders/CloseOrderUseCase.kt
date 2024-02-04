package com.alekseykostyunin.enot.domain.usecase.orders

import com.alekseykostyunin.enot.domain.repository.OrdersRepository

class CloseOrderUseCase(private val ordersRepository: OrdersRepository) {
    fun closeOrder(idOrder: String){
        ordersRepository.closeOrder(idOrder)
    }
}