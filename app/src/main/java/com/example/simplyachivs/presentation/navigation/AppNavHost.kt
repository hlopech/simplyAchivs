package com.example.simplyachivs.presentation.navigation

import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.simplyachivs.DetailTaskScreen
import com.example.simplyachivs.domain.model.achievement.Achievement
import com.example.simplyachivs.presentation.achievements.AchievementsScreen
import com.example.simplyachivs.presentation.analytics.AnalyticsScreen
import com.example.simplyachivs.presentation.asceticism.AsceticismScreen
import com.example.simplyachivs.presentation.achievements.GlobalAchievementViewModel
import com.example.simplyachivs.presentation.components.AchievementUnlockedBanner
import com.example.simplyachivs.presentation.goal.GoalScreen
import com.example.simplyachivs.presentation.goal.addGoal.AddGoalScreen
import com.example.simplyachivs.presentation.goal.goalDetails.GoalDetails
import com.example.simplyachivs.presentation.home.HomeScreen
import com.example.simplyachivs.presentation.login.LoginScreen
import com.example.simplyachivs.presentation.profile.ProfileScreen
import com.example.simplyachivs.presentation.settings.SettingsScreen
import com.example.simplyachivs.presentation.shop.ShopScreen
import com.example.simplyachivs.presentation.shop.addAward.AddAwardScreen
import com.example.simplyachivs.presentation.splash.SplashScreen
import kotlinx.coroutines.delay

@Composable
fun AppNavHost(appState: AppState) {

    val navController = appState.navController
    val globalVm: GlobalAchievementViewModel = hiltViewModel()
    val context = LocalContext.current

    var bannerAchievement by remember { mutableStateOf<Achievement?>(null) }
    var bannerVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        globalVm.newAchievements.collect { achievement ->
            bannerAchievement = achievement
            bannerVisible = true
            // Vibrate with a short double-pulse pattern
            val vibrator = context.getSystemService(Vibrator::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(
                    VibrationEffect.createWaveform(longArrayOf(0, 80, 60, 150), -1)
                )
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(longArrayOf(0, 80, 60, 150), -1)
            }
            delay(3500)
            bannerVisible = false
            delay(400) // wait for exit animation before allowing next banner
            bannerAchievement = null
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        NavHost(navController = navController, startDestination = Splash) {
        composable<Splash> {
            SplashScreen(
                onNavigateToHome = {
                    navController.navigate(Home) {
                        popUpTo(Splash) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.navigate(Login) {
                        popUpTo(Splash) { inclusive = true }
                    }
                }
            )
        }

        composable<Login> {
            LoginScreen(
                onNavigateToHome = {
                    navController.navigate(Home) {
                        popUpTo(Login) { inclusive = true }
                    }
                }
            )
        }

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
                onOpenSettings = { navController.navigate(Settings) },
                onOpenAchievements = { navController.navigate(Achievements) },
                onOpenAnalytics = { navController.navigate(Analytics) },
                onOpenAsceticism = { navController.navigate(Asceticism) },
            )
        }

        composable<Achievements> {
            AchievementsScreen(onBack = { navController.navigateUp() })
        }

        composable<Analytics> {
            AnalyticsScreen(onBack = { navController.navigateUp() })
        }

        composable<Asceticism> {
            AsceticismScreen(onBack = { navController.navigateUp() })
        }

        composable<Settings> {
            SettingsScreen(
                onBack = { navController.navigateUp() }
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

        } // end NavHost

        AchievementUnlockedBanner(
            achievement = bannerAchievement,
            visible = bannerVisible,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .padding(top = 8.dp),
        )

    } // end Box

}