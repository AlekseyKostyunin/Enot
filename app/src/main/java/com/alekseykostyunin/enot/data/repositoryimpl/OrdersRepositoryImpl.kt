package com.alekseykostyunin.enot.data.repositoryimpl

import com.alekseykostyunin.enot.domain.entities.Order
import com.alekseykostyunin.enot.domain.repository.OrdersRepository

object OrdersRepositoryImpl : OrdersRepository {

    override fun getAllOrders(): List<Order> {
        TODO("Not yet implemented")
    }

    override fun addOrder(order: Order) {
        TODO("Not yet implemented")
    }

    override fun closeOrder(idOrder: String) {
        TODO("Not yet implemented")
    }

    override fun editHistoryOrder(idOrder: String, history: String) {
        TODO("Not yet implemented")
    }
}