package com.alekseykostyunin.enot.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
@Composable
fun NavGraphNotMenu(
    navController: NavHostController,
    regScreenContent: @Composable () -> Unit,
    authScreenContent: @Composable () -> Unit,
    resetScreenContent: @Composable () -> Unit,
){
    NavHost(
        navController = navController,
        startDestination = Destinations.Authorisation.route
    ){
        composable(Destinations.Registration.route) {
            regScreenContent()
        }
        composable(Destinations.Authorisation.route) {
            authScreenContent()
        }
        composable(Destinations.ResetPassword.route) {
            resetScreenContent()
        }
    }
}
@Composable
fun NavGraphWithMenu(
    navController: NavHostController,
    ordersScreenContent: @Composable () -> Unit,
    clientsScreenContent: @Composable () -> Unit,
    analyticsScreenContent: @Composable () -> Unit,
    userScreenContent: @Composable () -> Unit,
){
    NavHost(
        navController = navController,
        startDestination = NavigationItem.Orders.route
    ){
        composable(NavigationItem.Orders.route) {
            ordersScreenContent()
        }
        composable(NavigationItem.Clients.route) {
            clientsScreenContent()
        }
        composable(NavigationItem.Analytics.route) {
            analyticsScreenContent()
        }
        composable(NavigationItem.User.route) {
            userScreenContent()
        }
    }
}