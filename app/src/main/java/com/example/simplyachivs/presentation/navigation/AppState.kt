package com.example.simplyachivs.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Stable
class AppState(val navController: NavHostController) {

    fun navigateToTopLevel(destination: Any) {
        navController.navigate(destination) {
            launchSingleTop = true
            restoreState = true
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
        }
    }


}

@Composable
fun rememberAppState(): AppState {
    val navController = rememberNavController()
    return remember(navController) { AppState(navController) }
}