package com.alekseykostyunin.enot.domain

class EditHistoryOrderUseCase(private val orderRepository: OrderRepository) {
    fun editHistoryOrder(){
        orderRepository.editHistoryOrder()
    }
}