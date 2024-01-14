package com.alekseykostyunin.enot.domain

class CloseOrderUseCase(private val orderRepository: OrderRepository) {
    fun closeOrder(){
        orderRepository.closeOrder()
    }
}