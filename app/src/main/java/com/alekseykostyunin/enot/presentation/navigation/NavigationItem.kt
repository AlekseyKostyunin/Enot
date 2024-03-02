package com.alekseykostyunin.enot.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Star
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavigationItem(
    val route: String,
    val title: String? = null,
    val icon: ImageVector? = null
) {
    data object Orders : NavigationItem(
        route = ROUTE_ORDERS_SCREEN,
        title = "Заказы",
        icon = Icons.Outlined.Home
    )

    data object Clients : NavigationItem(
        route = ROUTE_CLIENTS_SCREEN,
        title = "Клиенты",
        icon = Icons.Outlined.AccountCircle
    )

    data object Analytics : NavigationItem(
        route = ROUTE_ANALYTICS_SCREEN,
        title = "Финансы",
        icon = Icons.Outlined.Star
    )

    data object User : NavigationItem(
        route = ROUTE_USER_SCREEN,
        title = "Профиль",
        icon = Icons.Outlined.Settings
    )

    data object AllOrders : NavigationItem(
        route = ROUTE_ALL_ORDERS_SCREEN
    )

    data object AddOrder : NavigationItem(
        route = ROUTE_ADD_ORDERS_SCREEN
    )

    data object OneOrder : NavigationItem(
        route = ROUTE_ONE_ORDER_SCREEN
    )

    data object EditOrder : NavigationItem(
        route = ROUTE_EDIT_ORDER_SCREEN
    )

    private companion object{
        const val ROUTE_ORDERS_SCREEN = "orders_screen"
        const val ROUTE_CLIENTS_SCREEN = "clients_screen"
        const val ROUTE_ANALYTICS_SCREEN = "analytics_screen"
        const val ROUTE_USER_SCREEN = "user_screen"
        const val ROUTE_ALL_ORDERS_SCREEN = "all_orders_screen"
        const val ROUTE_ADD_ORDERS_SCREEN = "add_order_screen"
        const val ROUTE_ONE_ORDER_SCREEN = "one_order_screen"
        const val ROUTE_EDIT_ORDER_SCREEN = "edit_order_screen"
    }
}

