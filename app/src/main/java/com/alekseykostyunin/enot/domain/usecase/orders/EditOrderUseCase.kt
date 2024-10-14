package com.alekseykostyunin.enot.domain.usecase.orders

import com.alekseykostyunin.enot.domain.repository.OrdersRepository

class EditOrderUseCase(private val ordersRepository: OrdersRepository) {
    fun editOrder(idOrder: String, history: String){
        ordersRepository.editOrder(idOrder, history)
    }
}