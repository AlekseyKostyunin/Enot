package com.alekseykostyunin.enot.presentation.navigation


sealed class OrdersScreenState {
    //    object InitialState : OrdersScreenState()
    data object OrdersState : OrdersScreenState()
    data object AddOrderState : OrdersScreenState()
    data object OneOrderState : OrdersScreenState()

}
sealed class OrdersScreenState2(
    val route: String
) {
    data object OrdersState : OrdersScreenState2(
        route = ROUTE_ORDERS_SCREEN_NAV
    )
    data object AddOrderState : OrdersScreenState2(
        route = ROUTE_ADD_ORDERS_SCREEN
    )
    data object OneOrderState : OrdersScreenState2(
        route = ROUTE_ONE_ORDER_SCREEN
    )

    private companion object{
        const val ROUTE_ORDERS_SCREEN_NAV = "orders_screen_nav"
        const val ROUTE_ADD_ORDERS_SCREEN = "add_orders_screen"
        const val ROUTE_ONE_ORDER_SCREEN = "one_order_screen"
    }

}