package com.alekseykostyunin.enot.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navigation
import com.alekseykostyunin.enot.R
import com.alekseykostyunin.enot.presentation.screens.AddOrderScreen
import com.alekseykostyunin.enot.presentation.screens.AllClientsScreen
import com.alekseykostyunin.enot.presentation.screens.AllOrdersScreen
import com.alekseykostyunin.enot.presentation.screens.AnalyticsScreen
import com.alekseykostyunin.enot.presentation.screens.AuthScreen
import com.alekseykostyunin.enot.presentation.screens.EditClientScreen
import com.alekseykostyunin.enot.presentation.screens.EditOrderScreen
import com.alekseykostyunin.enot.presentation.screens.OneClientAllOrdersScreen
import com.alekseykostyunin.enot.presentation.screens.OneOrderScreen
import com.alekseykostyunin.enot.presentation.screens.PrivacyPolicyScreen
import com.alekseykostyunin.enot.presentation.screens.RegScreen
import com.alekseykostyunin.enot.presentation.screens.ResetPasswordScreen
import com.alekseykostyunin.enot.presentation.screens.UserScreen
import com.alekseykostyunin.enot.presentation.viewmodels.ClientsViewModel
import com.alekseykostyunin.enot.presentation.viewmodels.MainViewModel
import com.alekseykostyunin.enot.presentation.viewmodels.OrdersViewModel
import com.alekseykostyunin.enot.ui.theme.Purple40
import java.util.concurrent.ExecutorService

@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun StartNavigation(
    mainViewModel: MainViewModel,
    ordersViewModel: OrdersViewModel,
    clientsViewModel: ClientsViewModel,
    requestCameraPermission: () -> Unit,
    requestContactsPermission: () -> Unit,
    requestCallPhonePermission: () -> Unit,
    cameraExecutor: ExecutorService,
    getContact: () -> Unit
) {
    val statusAuth = mainViewModel.isAuthorized.observeAsState()
    val navigationState = rememberNavigationState()
    val isShowBottomBar = ordersViewModel.isShowBottomBar.observeAsState()
    val startDestination: String = if (statusAuth.value == true) Destinations.Orders.route
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
                        navigationState
                    )
                },
                resetScreenContent = {
                    ResetPasswordScreen(
                        navigationState
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
                        ordersViewModel,
                        clientsViewModel
                    )
                },
                oneOrderScreenContent = {
                    OneOrderScreen(
                        navigationState,
                        ordersViewModel,
                        requestCameraPermission,
                        requestCallPhonePermission,
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
                editClientScreenContent = {
                    ordersViewModel.notShowBottomBar()
                    EditClientScreen(
                        navigationState,
                        clientsViewModel
                    )
                },
                allClientsScreenContent = {
                    ordersViewModel.showBottomBar()
                    AllClientsScreen(
                        navigationState,
                        clientsViewModel,
                        getContact,
                        requestContactsPermission
                    )
                },
                oneClientAllOrdersScreenContent = {
                    ordersViewModel.showBottomBar()
                    OneClientAllOrdersScreen(
                        navigationState,
                        ordersViewModel,
                        clientsViewModel,
                        requestCallPhonePermission
                    )
                },
                analyticsScreenContent = {
                    AnalyticsScreen(
                        ordersViewModel
                    )
                },
                userScreenContent = {
                    UserScreen(
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
    val context = LocalContext.current
    NavigationBar(
        containerColor = Color.White,

        ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        screens.forEach { screen ->
            val selected = navBackStackEntry?.destination?.hierarchy?.any {
                it.route == screen.route
            } ?: false
            NavigationBarItem(
                label = { Text(text = screen.title!!) },
                icon = {
                    if (screen.route == Destinations.Orders.route) {
                        BadgedBox(
                            badge = {
                                Badge(
                                    containerColor = Purple40,
                                    contentColor = Color.White
                                ) {
                                    val badgeNumber = countActiveOrders.value.toString()
                                    Text(
                                        badgeNumber,
                                        modifier = Modifier.semantics {
                                            contentDescription = context.getString(
                                                R.string.desc_active_orders,
                                                badgeNumber
                                            )
                                        }
                                    )
                                }
                            }) {
                            Icon(
                                imageVector = screen.icon!!,
                                contentDescription = ""
                            )
                        }
                    } else {
                        Icon(
                            imageVector = screen.icon!!,
                            contentDescription = ""
                        )
                    }
                },
                selected = selected,
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
                    selectedTextColor = Color(0xFF04293A),
                    unselectedIconColor = Color.Gray,
                    selectedIconColor = Color(0xFF04293A)
                ),
                alwaysShowLabel = true
            )
        }
    }
}

sealed class Destinations(
    val route: String,
    val title: String? = null,
    val icon: ImageVector? = null
) {
    data object Authorisation : Destinations(ROUTE_AUTHORISATION_SCREEN)
    data object Registration : Destinations(ROUTE_REGISTRATION_SCREEN)
    data object ResetPassword : Destinations(ROUTE_RESET_PASSWORD_SCREEN)
    data object Orders : Destinations(ROUTE_ORDERS_SCREEN, "Заказы", Icons.Outlined.Home)
    data object AllOrders : Destinations(ROUTE_ALL_ORDERS_SCREEN)
    data object Clients : Destinations(ROUTE_CLIENTS_SCREEN, "Клиенты", Icons.Sharp.People)
    data object AllClients : Destinations(ROUTE_ALL_CLIENTS_SCREEN)
    data object OneClientAllOrders : Destinations(ROUTE_ONE_CLIENT_ALL_ORDERS_SCREEN)
    data object Analytics : Destinations(ROUTE_ANALYTICS_SCREEN, "Аналитика", Icons.Sharp.Insights)
    data object User : Destinations(ROUTE_USER_SCREEN, "Профиль", Icons.Outlined.Settings)
    data object AddOrder : Destinations(ROUTE_ADD_ORDERS_SCREEN)
    data object OneOrder : Destinations(ROUTE_ONE_ORDER_SCREEN)
    data object EditOrder : Destinations(ROUTE_EDIT_ORDER_SCREEN)
    data object EditClient : Destinations(ROUTE_EDIT_CLIENT_SCREEN)
    data object PrivacyPolicy : Destinations(ROUTE_PRIVACY_POLICY_SCREEN)
    private companion object {
        const val ROUTE_AUTHORISATION_SCREEN = "authorisation_screen"
        const val ROUTE_REGISTRATION_SCREEN = "registration_screen"
        const val ROUTE_RESET_PASSWORD_SCREEN = "reset_screen"
        const val ROUTE_ORDERS_SCREEN = "orders_screen"
        const val ROUTE_CLIENTS_SCREEN = "clients_screen"
        const val ROUTE_ONE_CLIENT_ALL_ORDERS_SCREEN = "one_client_all_orders_screen"
        const val ROUTE_ANALYTICS_SCREEN = "analytics_screen"
        const val ROUTE_USER_SCREEN = "user_screen"
        const val ROUTE_ALL_ORDERS_SCREEN = "all_orders_screen"
        const val ROUTE_ALL_CLIENTS_SCREEN = "all_clients_screen"
        const val ROUTE_ADD_ORDERS_SCREEN = "add_order_screen"
        const val ROUTE_ONE_ORDER_SCREEN = "one_order_screen"
        const val ROUTE_EDIT_ORDER_SCREEN = "edit_order_screen"
        const val ROUTE_EDIT_CLIENT_SCREEN = "edit_client_screen"
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
    editClientScreenContent: @Composable () -> Unit,
    allClientsScreenContent: @Composable () -> Unit,
    oneClientAllOrdersScreenContent: @Composable () -> Unit,
    analyticsScreenContent: @Composable () -> Unit,
    userScreenContent: @Composable () -> Unit,
    privacyPolicyScreenContent: @Composable () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
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
        ) {
            composable(Destinations.AllOrders.route) {
                allOrdersScreenContent()
            }
            composable(Destinations.AddOrder.route) {
                addOrderScreenContent()
            }
            composable(Destinations.OneOrder.route) {
                oneOrderScreenContent()
            }
            composable(Destinations.EditOrder.route) {
                editOrderScreenContent()
            }
        }
        navigation(
            route = Destinations.Clients.route,
            startDestination = Destinations.AllClients.route
        ) {
            composable(Destinations.AllClients.route) {
                allClientsScreenContent()
            }
            composable(Destinations.OneClientAllOrders.route) {
                oneClientAllOrdersScreenContent()
            }
            composable(Destinations.EditClient.route) {
                editClientScreenContent()
            }
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