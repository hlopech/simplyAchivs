package com.example.simplyachivs.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.simplyachivs.presentation.shop.addAward.AddAwardScreen
import com.example.simplyachivs.presentation.goal.addGoal.AddGoalScreen
import com.example.simplyachivs.DetailTaskScreen
import com.example.simplyachivs.presentation.home.HomeScreen
import com.example.simplyachivs.presentation.profile.ProfileScreen
import com.example.simplyachivs.presentation.shop.ShopScreen
import com.example.simplyachivs.presentation.goal.GoalScreen
import com.example.simplyachivs.presentation.goal.goalDetails.GoalDetails

@Composable
fun AppNavHost(appState: AppState) {

    val navController = appState.navController

    NavHost(navController = navController, startDestination = Home) {
        composable<Home> {
            HomeScreen(
                onOpenProfile = { navController.navigate((Profile)) })
        }

        composable<DetailTask> { entry ->
            val args: DetailTask = entry.toRoute()
            DetailTaskScreen(
                taskId = args.taskId,
                onBack = { navController.navigateUp() }
            )
        }

        composable<GoalDetails> { entry ->
            val args: GoalDetails = entry.toRoute()
            GoalDetails(goalId = args.goalId,
                onBack = { navController.navigateUp() }
                )

        }
        composable<Shop> {
            ShopScreen(onAddAward = { navController.navigate(AddAward) })
        }
        composable<Profile> {
            ProfileScreen(
                onBack = { navController.navigateUp() },
            )
        }

        composable<Target> {
            GoalScreen(
                onAddNewTarget = { navController.navigate(AddTarget) },
                onOpenGoal = { navController.navigate(GoalDetails(it)) },
            )
        }
        composable<AddTarget> {
            AddGoalScreen(
                onBack = { navController.navigateUp() },
            )
        }
        composable<AddAward> {
            AddAwardScreen(
                onBack = { navController.navigate(Shop) }
            )
        }


    }

}