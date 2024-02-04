package com.alekseykostyunin.enot.presentation.view

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.alekseykostyunin.enot.data.firebase.MyFirebaseAuth
import com.alekseykostyunin.enot.presentation.auth.Auth
import com.alekseykostyunin.enot.presentation.auth.Reg
import com.alekseykostyunin.enot.presentation.auth.ResetPassword
import com.alekseykostyunin.enot.presentation.menu.SetMenu

@Composable
fun StartNavigation() {
    val isUserAuth = MyFirebaseAuth.currentUser()
    Log.d("TEST_currentUser", isUserAuth.toString())
    if(isUserAuth) {
        SetMenu()
    } else{
        val navController: NavHostController = rememberNavController()
        NavigationGraphAuth(navController)
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
            ResetPassword(navController)
        }
        composable(Destinations.SetMenu.route) {
            SetMenu()
        }
    }
}

@Composable
fun NavGraphWithMenu(navController: NavHostController){
    NavHost(navController, startDestination = Destinations.Orders.route) {
        composable(Destinations.Orders.route) {
            OrdersScreen(navController)
        }
        composable(Destinations.Clients.route) {
            Clients(navController)
        }
        composable(Destinations.Analytics.route) {
            AnalyticsScreen(navController)
        }
        composable(Destinations.User.route) {
            UserScreen(navController)
        }
        composable(Destinations.AddOrder.route) {
            AddOrderScreen(navController)
        }
        composable(Destinations.Authorisation.route) {
            Auth(navController)
        }
    }
}