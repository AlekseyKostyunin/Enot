package com.alekseykostyunin.enot.data.repositoryimpl

import com.alekseykostyunin.enot.data.firebase.AppFirebase
import com.alekseykostyunin.enot.domain.entities.Order
import com.alekseykostyunin.enot.domain.repository.OrdersRepository
import kotlinx.coroutines.flow.Flow

object OrdersRepositoryImpl : OrdersRepository {
    override fun getAllOrders(): Flow<List<Order>> =  AppFirebase.getAllOrders()
    override fun addOrder(order: Order) = AppFirebase.addOrder(order)
    override fun addPhotoOrder(photoUri: String, order: Order): Flow<Order> = AppFirebase.addPhotoOrder(photoUri, order)
    override fun closeOrder(order: Order): Flow<Order> = AppFirebase.closeOrder(order)
    override fun editOrder(order: Order) = AppFirebase.editOrder(order)
    override fun addHistoryStep(order: Order, descStep: String): Flow<Order> = AppFirebase.addHistoryStep(order, descStep)
}