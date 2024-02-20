package com.alekseykostyunin.enot.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

class NavigationState(
    val navHostController: NavHostController
){
    fun navigateTo(route: String){
        navHostController.navigate(route) {
//            popUpTo(navHostController.graph.findStartDestination().id) {// будут удалены все экраны до стартового
            popUpTo(navHostController.graph.startDestinationId) {// будут удалены все экраны до стартового
                saveState = true // при удалении экранов из бекстека их стейт будет сохранен
            }
            launchSingleTop = true // хранить только верхний последний стейт экрана, не хранить дублирование
            restoreState = true // при возрате на этот экран восстановить стейт этого экрана
        }
    }
}

@Composable
fun rememberNavigationState(
    navHostController: NavHostController = rememberNavController()
): NavigationState{
    return remember{
        NavigationState(navHostController)
    }
}