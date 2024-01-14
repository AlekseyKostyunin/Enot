package com.alekseykostyunin.enot.domain

class AddOrderUseCase(private val orderRepository: OrderRepository) {

    fun addOrder(){
        orderRepository.addOrder()
    }
}