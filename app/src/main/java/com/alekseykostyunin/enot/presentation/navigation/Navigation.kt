package com.alekseykostyunin.enot.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.sharp.Insights
import androidx.compose.material.icons.sharp.People
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navigation
import com.alekseykostyunin.enot.presentation.screens.AddOrderScreen
import com.alekseykostyunin.enot.presentation.screens.AllOrdersScreen
import com.alekseykostyunin.enot.presentation.screens.AnalyticsScreen
import com.alekseykostyunin.enot.presentation.screens.AuthScreen
import com.alekseykostyunin.enot.presentation.screens.ClientsScreen
import com.alekseykostyunin.enot.presentation.screens.EditOrderScreen
import com.alekseykostyunin.enot.presentation.screens.OneOrderScreen
import com.alekseykostyunin.enot.presentation.screens.PrivacyPolicyScreen
import com.alekseykostyunin.enot.presentation.screens.RegScreen
import com.alekseykostyunin.enot.presentation.screens.ResetPasswordScreen
import com.alekseykostyunin.enot.presentation.screens.UserScreen2
import com.alekseykostyunin.enot.presentation.screens.UserScreen3
import com.alekseykostyunin.enot.presentation.viewmodels.ClientsViewModel
import com.alekseykostyunin.enot.presentation.viewmodels.MainViewModel
import com.alekseykostyunin.enot.presentation.viewmodels.OrdersViewModel
import java.util.concurrent.ExecutorService

@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun StartNavigation(
    mainViewModel: MainViewModel,
    ordersViewModel: OrdersViewModel,
    clientsViewModel: ClientsViewModel,
    requestCameraPermission: () -> Unit,
    requestContactsPermission: () -> Unit,
    cameraExecutor: ExecutorService,
    getContact: () -> Unit,
) {
    val statusAuth = mainViewModel.isAuthorized.observeAsState()
    val navigationState = rememberNavigationState()
    val isShowBottomBar = ordersViewModel.isShowBottomBar.observeAsState()
    val startDestination: String = if(statusAuth.value == true) Destinations.Orders.route
    else Destinations.Authorisation.route
    val snackBarHostState = remember { SnackbarHostState() }
    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        bottomBar = {
            if (isShowBottomBar.value == true && statusAuth.value == true) {
                BottomBar(
                    navController = navigationState.navHostController,
                    ordersViewModel
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .background(Color.White),
        ) {
            NavGraph(
                navigationState.navHostController,
                startDestination,
                authScreenContent = {
                    AuthScreen(
                        navigationState,
                        mainViewModel,
                        ordersViewModel,
                        snackBarHostState
                    )
                },
                regScreenContent = {
                    RegScreen(
                        navigationState,
                        snackBarHostState
                    )
                },
                resetScreenContent = {
                    ResetPasswordScreen(
                        navigationState,
                        snackBarHostState
                    )
                },
                allOrdersScreenContent = {
                    ordersViewModel.showBottomBar()
                    AllOrdersScreen(
                        navigationState,
                        ordersViewModel
                    )
                },
                addOrderScreenContent = {
                    ordersViewModel.notShowBottomBar()
                    AddOrderScreen(
                        navigationState,
                        ordersViewModel
                    )
                },
                oneOrderScreenContent = {
                    //ordersViewModel.showBottomBar()
                    OneOrderScreen(
                        navigationState,
                        mainViewModel,
                        ordersViewModel,
                        requestCameraPermission,
                        cameraExecutor
                    )
                },
                editOrderScreenContent = {
                    ordersViewModel.notShowBottomBar()
                    EditOrderScreen(
                        navigationState,
                        ordersViewModel
                    )
                },
                clientsScreenContent = {
                    ClientsScreen(
                        navigationState,
                        mainViewModel,
                        clientsViewModel,
                        getContact,
                        requestContactsPermission,
                    )
                },
                analyticsScreenContent = {
                    AnalyticsScreen(
                        ordersViewModel
                    )
                },
                userScreenContent = {
                    UserScreen3(
                        navigationState,
                        onClickButtonSighOut = {
                            ordersViewModel.notShowBottomBar()
                            mainViewModel.signOut()
                        }
                    )
                },
                privacyPolicyScreenContent = {
                    PrivacyPolicyScreen(
                        navigationState
                    )
                }
            )
        }
    }
}

@Composable
fun BottomBar(navController: NavHostController, ordersViewModel: OrdersViewModel) {
    val countActiveOrders = ordersViewModel.countActiveOrders.observeAsState()
    val screens = listOf(
        Destinations.Orders,
        Destinations.Clients,
        Destinations.Analytics,
        Destinations.User
    )
    NavigationBar(containerColor = Color.White) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        screens.forEach { screen ->
            val selected = navBackStackEntry?.destination?.hierarchy?.any {
                it.route == screen.route
            } ?: false

            NavigationBarItem(
                label = { Text(text = screen.title!!) },
                icon = {
                    if (screen.route == Destinations.Orders.route){
                        BadgedBox(
                            badge = {
                                Badge(
                                    containerColor = Color.Red,
                                    contentColor = Color.White
                                ) {
                                    val badgeNumber = countActiveOrders.value.toString()
                                    Text(
                                        badgeNumber,
                                        modifier = Modifier.semantics {
                                            contentDescription = "$badgeNumber new notifications"
                                        }
                                    )
                                }
                            }) {
                            Icon(
                                imageVector = screen.icon!!,
                                contentDescription = ""
                            )
                        }
                    } else{
                        Icon(
                            imageVector = screen.icon!!,
                            contentDescription = ""
                        )
                    }
                },
                selected = selected,
                onClick = {
                    navController.navigate(screen.route) {
                        //popUpTo(navController.graph.findStartDestination().id) {
                        popUpTo(navController.graph.last().id){
                            // будут удалены все экраны до стартового
                            // popUpTo(navController.graph.startDestinationId) {
                            // при удалении экранов из бекстека их стейт будет сохранен
                            saveState = false
                        }
//                        // хранить только верхний последний стейт экрана, не хранить дублирование
                        launchSingleTop = true
//                        // при возрате на этот экран восстановить стейт этого экрана
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    unselectedTextColor = Color.Gray,
                    selectedTextColor = Color(0xFF04293A),
                    unselectedIconColor = Color.Gray,
                    selectedIconColor = Color(0xFF04293A)
                ),
                alwaysShowLabel = true
            )
        }
    }
}

sealed class Destinations(val route: String, val title: String? = null, val icon: ImageVector? = null) {
    data object Authorisation : Destinations(ROUTE_AUTHORISATION_SCREEN)
    data object Registration :  Destinations(ROUTE_REGISTRATION_SCREEN)
    data object ResetPassword : Destinations(ROUTE_RESET_PASSWORD_SCREEN)

    data object Orders : Destinations(
        route = ROUTE_ORDERS_SCREEN,
        title = "Заказы",
        icon = Icons.Outlined.Home
    )

    data object AllOrders : Destinations(
        route = ROUTE_ALL_ORDERS_SCREEN
    )

    data object Clients : Destinations(
        route = ROUTE_CLIENTS_SCREEN,
        title = "Клиенты",
        icon = Icons.Sharp.People
    )

    data object Analytics : Destinations(
        route = ROUTE_ANALYTICS_SCREEN,
        title = "Финансы",
        icon = Icons.Sharp.Insights
    )

    data object User : Destinations(
        route = ROUTE_USER_SCREEN,
        title = "Профиль",
        icon = Icons.Outlined.Settings
    )

    data object AddOrder : Destinations(ROUTE_ADD_ORDERS_SCREEN)
    data object OneOrder : Destinations(ROUTE_ONE_ORDER_SCREEN)
    data object EditOrder : Destinations(ROUTE_EDIT_ORDER_SCREEN)
    data object PrivacyPolicy : Destinations(ROUTE_PRIVACY_POLICY_SCREEN)

    private companion object{
        const val ROUTE_AUTHORISATION_SCREEN = "authorisation_screen"
        const val ROUTE_REGISTRATION_SCREEN  = "registration_screen"
        const val ROUTE_RESET_PASSWORD_SCREEN = "reset_screen"
        const val ROUTE_ORDERS_SCREEN = "orders_screen"
        const val ROUTE_CLIENTS_SCREEN = "clients_screen"
        const val ROUTE_ANALYTICS_SCREEN = "analytics_screen"
        const val ROUTE_USER_SCREEN = "user_screen"
        const val ROUTE_ALL_ORDERS_SCREEN = "all_orders_screen"
        const val ROUTE_ADD_ORDERS_SCREEN = "add_order_screen"
        const val ROUTE_ONE_ORDER_SCREEN = "one_order_screen"
        const val ROUTE_EDIT_ORDER_SCREEN = "edit_order_screen"
        const val ROUTE_PRIVACY_POLICY_SCREEN = "privacy_policy"
    }
}

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String,
    authScreenContent: @Composable () -> Unit,
    regScreenContent: @Composable () -> Unit,
    resetScreenContent: @Composable () -> Unit,
    allOrdersScreenContent: @Composable () -> Unit,
    addOrderScreenContent: @Composable () -> Unit,
    oneOrderScreenContent: @Composable () -> Unit,
    editOrderScreenContent: @Composable () -> Unit,
    clientsScreenContent: @Composable () -> Unit,
    analyticsScreenContent: @Composable () -> Unit,
    userScreenContent: @Composable () -> Unit,
    privacyPolicyScreenContent: @Composable () -> Unit
){
    NavHost(
        navController = navController,
        startDestination = startDestination
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
        navigation(
            route = Destinations.Orders.route,
            startDestination = Destinations.AllOrders.route
        ){
            composable(Destinations.AllOrders.route){
                allOrdersScreenContent()
            }
            composable(Destinations.AddOrder.route){
                addOrderScreenContent()
            }
            composable(Destinations.OneOrder.route){
                oneOrderScreenContent()
            }
            composable(Destinations.EditOrder.route){
                editOrderScreenContent()
            }
        }
        composable(Destinations.Clients.route) {
            clientsScreenContent()
        }
        composable(Destinations.Analytics.route) {
            analyticsScreenContent()
        }
        composable(Destinations.User.route) {
            userScreenContent()
        }
        composable(Destinations.PrivacyPolicy.route) {
            privacyPolicyScreenContent()
        }
    }
}