package com.alekseykostyunin.enot.domain.usecase.orders

import com.alekseykostyunin.enot.domain.repository.OrdersRepository

class EditHistoryOrderUseCase(private val ordersRepository: OrdersRepository) {
    fun editHistoryOrder(idOrder: String, history: String){
        ordersRepository.editHistoryOrder(idOrder, history)
    }
}