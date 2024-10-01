package com.alekseykostyunin.enot.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val chartColorsChartPrize = listOf(Purple40, Purple80)
val columnColorsChartCountOrders = listOf(Purple40, Purple80, PurpleGrey80)

val gradient = Brush.horizontalGradient(
    0.0f to Color(0xFF04293A),
    1.0f to Color(0xFF781D42),
    startX = 1000.0f,
    endX = 0.0f
)
val gradient2 = Brush.horizontalGradient(
    0.0f to Purple40,
    1.0f to Purple80,
    startX = 1000.0f,
    endX = 0.0f
)