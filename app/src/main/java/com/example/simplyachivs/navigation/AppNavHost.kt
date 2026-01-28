package com.example.simplyachivs.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.simplyachivs.AddAwardScreen
import com.example.simplyachivs.AddTargetScreen
import com.example.simplyachivs.DetailTaskScreen
import com.example.simplyachivs.HomeScreen
import com.example.simplyachivs.ProfileScreen
import com.example.simplyachivs.ShopScreen
import com.example.simplyachivs.TargetScreen

@Composable
fun AppNavHost(appState: AppState) {

    val navController = appState.navController

    NavHost(navController = navController, startDestination = Home) {
        composable<Home> {
            HomeScreen(
                onAddTask = { navController.navigate(AddTask) },
                onOpenTask = { navController.navigate(DetailTask(it)) },
                onOpenProfile = { navController.navigate((Profile)) })
        }

        composable<DetailTask> { entry ->
            val args: DetailTask = entry.toRoute()
            DetailTaskScreen(
                taskId = args.taskId,
                onBack = { navController.navigateUp() }
            )
        }
        composable<Shop> {
            ShopScreen(onAddAward = { navController.navigate(AddAward) })
        }
        composable<Profile> {
            ProfileScreen()
        }

        composable<Target> {
            TargetScreen(
                onAddNewTarget = { navController.navigate(AddTarget) },
            )
        }
        composable<AddTarget> {
            AddTargetScreen(
                onBack = { navController.navigateUp() },
                onAddTarget = { navController.navigate(Target) }
            )
        }
        composable<AddAward> {
            AddAwardScreen(
                onBack = { navController.navigateUp() },
                onAddAward={navController.navigate(Shop)}
            )
        }


    }

}