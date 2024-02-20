package com.alekseykostyunin.enot.presentation.navigation


sealed class OrdersScreenState {
//    object InitialState : OrdersScreenState()
    data object OrdersState : OrdersScreenState()
    data object AddOrderState : OrdersScreenState()
    data object OneOrderState : OrdersScreenState()

}