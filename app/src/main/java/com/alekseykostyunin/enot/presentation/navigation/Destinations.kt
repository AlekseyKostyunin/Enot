package com.alekseykostyunin.enot.presentation.navigation

sealed class Destinations(val route: String) {
    data object Authorisation : Destinations(
        route = ROUTE_AUTHORISATION_SCREEN
    )
    data object Registration : Destinations(
        route = ROUTE_REGISTRATION_SCREEN
    )
    data object ResetPassword : Destinations(
        route = ROUTE_RESET_PASSWORD_SCREEN
    )
    private companion object{
        const val ROUTE_AUTHORISATION_SCREEN = "authorisation_screen"
        const val ROUTE_REGISTRATION_SCREEN  = "registration_screen"
        const val ROUTE_RESET_PASSWORD_SCREEN = "reset_screen"
    }
}