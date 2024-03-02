package com.alekseykostyunin.enot.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation

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

@Composable
fun NavGraphWithMenu2(
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
        navigation(
            startDestination = OrdersScreenState2.OrdersState.route,
            route = NavigationItem.Orders.route
        ){
            composable(OrdersScreenState2.OrdersState.route){
                ordersScreenContent()
            }
            composable(OrdersScreenState2.AddOrderState.route){
                ordersScreenContent()
            }
            composable(OrdersScreenState2.OrdersState.route){
                ordersScreenContent()
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