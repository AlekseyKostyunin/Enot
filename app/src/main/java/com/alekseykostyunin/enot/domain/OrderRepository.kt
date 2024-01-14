package com.alekseykostyunin.enot.domain

interface OrderRepository {

    fun addOrder()
    fun closeOrder()
    fun editHistoryOrder()

}