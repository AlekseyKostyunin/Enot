package com.alekseykostyunin.enot.presentation.view

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Star
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Destinations(
    val route: String,
    val title: String? = null,
    val icon: ImageVector? = null
) {
    data object Authorisation : Destinations(
        route = "authorisation_screen"
    )

    data object Registration : Destinations(
        route = "registration_screen"
    )

    data object ResetPassword : Destinations(
        route = "reset_screen"
    )

    data object Orders : Destinations(
        route = "orders_screen",
        title = "Заказы",
        icon = Icons.Outlined.Home
    )

    data object Clients : Destinations(
        route = "clients_screen",
        title = "Клиенты",
        icon = Icons.Outlined.AccountCircle
    )

    data object Analytics : Destinations(
        route = "analytics_screen",
        title = "Финансы",
        icon = Icons.Outlined.Star
    )

    data object User : Destinations(
        route = "user_screen",
        title = "Профиль",
        icon = Icons.Outlined.Settings
    )

    data object AddOrder : Destinations(
        route = "addOrder_screen"
    )

    data object SetMenu : Destinations(
        route = "menu_screen"
    )


}