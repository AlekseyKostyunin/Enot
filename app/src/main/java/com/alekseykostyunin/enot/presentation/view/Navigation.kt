package com.alekseykostyunin.enot.presentation.view

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.alekseykostyunin.enot.presentation.auth.Auth
import com.alekseykostyunin.enot.presentation.auth.Reg
import com.alekseykostyunin.enot.presentation.menu.SetMenu

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(navController, startDestination = Destinations.Orders.route) {
        composable(Destinations.Orders.route) {
            Orders()
        }
        composable(Destinations.Clients.route) {
            Clients()
        }
        composable(Destinations.Analytics.route) {
            AddOrder()
        }
        composable(Destinations.User.route) {
            Order()
        }
    }
}
@Composable
fun NavigationGraphAuth(navController: NavHostController) {
    NavHost(navController, startDestination = Destinations.Authorisation.route) {
        composable(Destinations.Registration.route) {
            Reg(navController)
        }
        composable(Destinations.Authorisation.route) {
            Auth(navController)
        }
        composable(Destinations.ResetPassword.route) {
            Auth(navController)
        }
        composable(Destinations.SetMenu.route) {
            SetMenu()
        }
    }
}