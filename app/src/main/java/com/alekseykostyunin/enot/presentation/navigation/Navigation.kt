package com.alekseykostyunin.enot.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.alekseykostyunin.enot.presentation.auth.Auth
import com.alekseykostyunin.enot.presentation.auth.Reg
import com.alekseykostyunin.enot.presentation.auth.ResetPassword
import com.alekseykostyunin.enot.presentation.screens.AnalyticsScreen
import com.alekseykostyunin.enot.presentation.screens.ClientsScreen
import com.alekseykostyunin.enot.presentation.screens.OrdersScreenMenu
import com.alekseykostyunin.enot.presentation.screens.UserScreen
import com.alekseykostyunin.enot.presentation.viewmodels.OrdersViewModel
import com.alekseykostyunin.enot.presentation.viewmodels.StartViewModel

@Composable
fun StartNavigation() {
    val startViewModel: StartViewModel = viewModel()
    val screenState = startViewModel.startScreenState.observeAsState()
    when(screenState.value){
        is StartScreenState.NotAuthScreenState -> {
            NotAuthScreen(startViewModel)
        }
        is StartScreenState.AuthScreenState -> {
            AuthScreen(startViewModel)
        }
        else -> {
            NotAuthScreen(startViewModel)
        }
    }
}

@Composable
fun NotAuthScreen(startViewModel: StartViewModel){
    val navigationState = rememberNavigationState() // это моя функция
    Scaffold { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            NavGraphNotMenu(navigationState.navHostController,
                regScreenContent = {
                    Reg(navigationState.navHostController)
                },
                authScreenContent = {
                    Auth(
                        navigationState.navHostController,
                        startViewModel
                    )
                },
                resetScreenContent = {
                    ResetPassword(navigationState.navHostController)
                }
            )
        }
    }
}
@Composable
fun AuthScreen(startViewModel: StartViewModel){
    val navigationState = rememberNavigationState() // это моя функция
    val buttonsVisible = remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            BottomBar(
                navController = navigationState.navHostController,
                //state = buttonsVisible
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            NavGraphWithMenu(
                navigationState.navHostController,
                ordersScreenContent = { OrdersScreenMenu()},
                clientsScreenContent = { ClientsScreen() },
                analyticsScreenContent = { AnalyticsScreen() },
                userScreenContent = {
                    UserScreen(
                        onClickButtonSighOut = {startViewModel.signOut()}
                    )
                }
            )
        }
    }
}

@Composable
fun BottomBar(
    navController: NavHostController,
    //state: MutableState<Boolean>,
    //modifier: Modifier = Modifier
) {
    val screens = listOf(
        NavigationItem.Orders,
        NavigationItem.Clients,
        NavigationItem.Analytics,
        NavigationItem.User
    )

    NavigationBar(
        // modifier = modifier,
        containerColor = Color.White
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route // получаем ссылку на текущий экран
        screens.forEach { screen ->
            NavigationBarItem(
                label = { Text(text = screen.title!!) },
                icon = { Icon(imageVector = screen.icon!!, contentDescription = "") },
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {// будут удалены все экраны до стартового
                            saveState = true // при удалении экранов из бекстека их стейт будет сохранен
                        }
                        launchSingleTop = true // хранить только верхний последний стейт экрана, не хранить дублирование
                        restoreState = true // при возрате на этот экран восстановить стейт этого экрана
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