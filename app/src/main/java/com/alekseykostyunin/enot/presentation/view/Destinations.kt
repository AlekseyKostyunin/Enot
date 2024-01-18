package com.alekseykostyunin.enot.presentation.view

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Destinations(
    val route: String,
    val title: String? = null,
    val icon: ImageVector? = null
) {
    object User : Destinations(
        route = "user_screen",
        title = "Профиль",
        icon = Icons.Outlined.Settings
    )

    object SetMenu : Destinations(
        route = "menu_screen"
    )
    object Orders : Destinations(
        route = "orders_screen",
        title = "Заказы",
        icon = Icons.Outlined.Home
    )

    data object Clients : Destinations(
        route = "clients_screen",
        title = "Клиенты",
        icon = Icons.Outlined.AccountCircle
    )

    object Analytics : Destinations(
        route = "analytics_screen",
        title = "Финансы",
        icon = Icons.Outlined.Info
    )

    object Registration : Destinations(
        route = "registration_screen"
    )
    object Authorisation : Destinations(
        route = "authorisation_screen"
    )
    object ResetPassword : Destinations(
        route = "reset_screen"
    )


}