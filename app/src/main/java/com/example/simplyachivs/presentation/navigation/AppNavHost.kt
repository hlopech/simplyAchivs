package com.example.simplyachivs.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.simplyachivs.presentation.shop.addAward.AddAwardScreen
import com.example.simplyachivs.presentation.goal.addGoal.AddTargetScreen
import com.example.simplyachivs.DetailTaskScreen
import com.example.simplyachivs.presentation.home.HomeScreen
import com.example.simplyachivs.presentation.profile.ProfileScreen
import com.example.simplyachivs.presentation.shop.ShopScreen
import com.example.simplyachivs.presentation.goal.TargetScreen

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