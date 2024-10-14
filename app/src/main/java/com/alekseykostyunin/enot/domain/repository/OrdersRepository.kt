package com.alekseykostyunin.enot.domain.repository

import com.alekseykostyunin.enot.domain.entities.HistoryStep
import com.alekseykostyunin.enot.domain.entities.Order

interface OrdersRepository {

    fun getAllOrders(): List<Order>
    fun addOrder(order: Order)
    fun closeOrder(idOrder: String)
    fun editOrder(idOrder: String, history: String)
    fun addHistoryStep(idOrder: String, historyStep: HistoryStep)

}