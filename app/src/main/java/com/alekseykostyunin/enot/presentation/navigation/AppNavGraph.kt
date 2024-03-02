package com.alekseykostyunin.enot.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation

@Composable // готово
fun NavGraphNotMenu(
    navController: NavHostController,
    authScreenContent: @Composable () -> Unit,
    regScreenContent: @Composable () -> Unit,
    resetScreenContent: @Composable () -> Unit,
){
    NavHost(
        navController = navController,
        startDestination = Destinations.Authorisation.route
    ){
        composable(Destinations.Authorisation.route) {
            authScreenContent()
        }
        composable(Destinations.Registration.route) {
            regScreenContent()
        }
        composable(Destinations.ResetPassword.route) {
            resetScreenContent()
        }
    }
}

@Composable
fun NavGraphWithMenu(
    navController: NavHostController,

    allOrdersScreenContent: @Composable () -> Unit,
    addOrderScreenContent: @Composable () -> Unit,
    oneOrdersScreenContent: @Composable () -> Unit,

    clientsScreenContent: @Composable () -> Unit,
    analyticsScreenContent: @Composable () -> Unit,
    userScreenContent: @Composable () -> Unit,
){
    NavHost(
        navController = navController,
        startDestination = NavigationItem.Orders.route
    ){
        navigation(
            route = NavigationItem.Orders.route,
            startDestination = NavigationItem.AllOrders.route
        ){
            composable(NavigationItem.AllOrders.route){
                allOrdersScreenContent()
            }
            composable(NavigationItem.AddOrder.route){
                addOrderScreenContent()
            }
            composable(NavigationItem.OneOrder.route){
                oneOrdersScreenContent()
            }
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