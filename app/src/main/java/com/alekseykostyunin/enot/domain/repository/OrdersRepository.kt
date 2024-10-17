package com.alekseykostyunin.enot.domain.repository

import com.alekseykostyunin.enot.domain.entities.HistoryStep
import com.alekseykostyunin.enot.domain.entities.Order
import kotlinx.coroutines.flow.Flow

interface OrdersRepository {
    fun getAllOrders(): Flow<List<Order>>
    fun addOrder(order: Order)
    fun addPhotoOrder(photoUri: String, order: Order): Flow<Order>
    fun closeOrder(order: Order): Flow<Order>
    fun editOrder(order: Order)
    fun addHistoryStep(order: Order, descStep: String): Flow<Order>
}