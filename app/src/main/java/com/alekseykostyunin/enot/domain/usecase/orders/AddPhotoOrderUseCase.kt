package com.alekseykostyunin.enot.domain.usecase.orders

import com.alekseykostyunin.enot.domain.entities.Order
import com.alekseykostyunin.enot.domain.repository.OrdersRepository
import kotlinx.coroutines.flow.Flow

class AddPhotoOrderUseCase(private val ordersRepository: OrdersRepository) {
    fun invoke(photoUri: String, order: Order): Flow<Order> = ordersRepository.addPhotoOrder(photoUri, order)
}