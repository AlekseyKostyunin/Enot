package com.alekseykostyunin.enot.data.repositoryimpl

import com.alekseykostyunin.enot.data.firebase.Firebase
import com.alekseykostyunin.enot.domain.entities.HistoryStep
import com.alekseykostyunin.enot.domain.entities.Order
import com.alekseykostyunin.enot.domain.repository.OrdersRepository
import kotlinx.coroutines.flow.Flow

object OrdersRepositoryImpl : OrdersRepository {
    override fun getAllOrders(): Flow<List<Order>> =  Firebase.getAllOrders()
    override fun addOrder(order: Order) = Firebase.addOrder(order)
    override fun addPhotoOrder(photoUri: String, order: Order): Flow<Order> = Firebase.addPhotoOrder(photoUri, order)
    override fun closeOrder(order: Order): Flow<Order> = Firebase.closeOrder(order)
    override fun editOrder(order: Order) = Firebase.editOrder(order)
    override fun addHistoryStep(order: Order, descStep: String): Flow<Order> = Firebase.addHistoryStep(order, descStep)
}