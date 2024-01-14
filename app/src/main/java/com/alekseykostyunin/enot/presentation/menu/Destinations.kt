package com.alekseykostyunin.enot.presentation.menu

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Destinations(
    val route: String,
    val title: String? = null,
    val icon: ImageVector? = null
) {
    object Orders : Destinations(
        route = "orders_screen",
        title = "Заказы",
        icon = Icons.Outlined.Home
    )

    object Clients : Destinations(
        route = "clients_screen",
        title = "Клиенты",
        icon = Icons.Outlined.AccountCircle
    )

    object Analytics : Destinations(
        route = "analytics_screen",
        title = "Финансы",
        icon = Icons.Outlined.Info
    )
}