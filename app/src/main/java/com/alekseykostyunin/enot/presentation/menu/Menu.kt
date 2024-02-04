package com.alekseykostyunin.enot.presentation.menu

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.alekseykostyunin.enot.presentation.auth.Auth
import com.alekseykostyunin.enot.presentation.view.AddOrderScreen
import com.alekseykostyunin.enot.presentation.view.AnalyticsScreen
import com.alekseykostyunin.enot.presentation.view.Clients
import com.alekseykostyunin.enot.presentation.view.Destinations
import com.alekseykostyunin.enot.presentation.view.NavGraphWithMenu
import com.alekseykostyunin.enot.presentation.view.OrdersScreen
import com.alekseykostyunin.enot.presentation.view.UserScreen

@Composable
fun SetMenu(
) {
    val navController : NavHostController = rememberNavController()
    val bottomBarHeight = 40.dp
    val bottomBarOffsetHeightPx = remember { mutableStateOf(0f) }
    val buttonsVisible = remember { mutableStateOf(true) }

    Scaffold(
        bottomBar = {
            BottomBar(
                navController = navController,
                state = buttonsVisible,
                modifier = Modifier,
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues), ) {
            NavGraphWithMenu(navController)
        }
    }
}

@Composable
fun BottomBar(
    navController: NavHostController, state: MutableState<Boolean>, modifier: Modifier = Modifier
) {
    val screens =
        listOf(Destinations.Orders, Destinations.Clients, Destinations.Analytics, Destinations.User)

    NavigationBar(
        modifier = modifier,
//        containerColor = Color.LightGray,
        containerColor = Color.White,
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        screens.forEach { screen ->

            NavigationBarItem(
                label = { Text(text = screen.title!!) },
                icon = { Icon(imageVector = screen.icon!!, contentDescription = "") },
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    unselectedTextColor = Color.Gray,
                    selectedTextColor = Color.Black,
                    unselectedIconColor = Color.Gray,
                    selectedIconColor = Color.Black
                ),
            )
        }
    }
}

