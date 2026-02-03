package com.example.simplyachivs.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.simplyachivs.R
import com.example.simplyachivs.ui.theme.BottomNavBackground
import com.example.simplyachivs.ui.theme.MainBlue


@Composable
fun BottomNavigationBar(appState: AppState) {

    val backStackEntry = appState.navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry.value?.destination
    val showBottomBar = currentDestination?.hasRoute(DetailTask::class) != true
    if (showBottomBar) {
        NavigationBar(
            tonalElevation = 20.dp,
            containerColor = Color.Transparent,
            contentColor = Color.Transparent,
            modifier = Modifier.height(100.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier
                    .background(BottomNavBackground)
                    .fillMaxWidth()
                    .height(60.dp)
                    .border(1.dp, color = MainBlue)
            ) {

                IconButton(onClick = {
                    appState.navigateToTopLevel(destination = Home)
                }) {
                    Icon(
                        painter = painterResource(R.drawable.home_icon),
                        contentDescription = "home",
                        tint = when (currentDestination?.hasRoute(Home::class)) {
                            true -> MainBlue
                            else -> Color.Gray
                        },
                        modifier = Modifier.size(40.dp)
                    )
                }
                IconButton(onClick = { appState.navigateToTopLevel(destination = Target) }) {
                    Icon(
                        painter = painterResource(R.drawable.target_icon),
                        contentDescription = "target",
                        tint = when (currentDestination?.hasRoute(Target::class)) {
                            true -> MainBlue
                            else -> Color.Gray
                        },
                        modifier = Modifier.size(40.dp)

                    )

                }
                IconButton(
                    onClick =
                        { appState.navigateToTopLevel(destination = Shop) }) {
                    Icon(
                        painter = painterResource(R.drawable.shop_icon),
                        contentDescription = "shop",
                        tint = when (currentDestination?.hasRoute(Shop::class)) {
                            true -> MainBlue
                            else -> Color.Gray
                        },
                        modifier = Modifier.size(40.dp)

                    )
                }

                IconButton(
                    onClick =
                        { appState.navigateToTopLevel(destination = Profile) }) {
                    Icon(
                        painter = painterResource(R.drawable.profile_icon),
                        contentDescription = "profile",
                        tint = when (currentDestination?.hasRoute(Profile::class)) {
                            true -> MainBlue
                            else -> Color.Gray
                        },
                        modifier = Modifier.size(40.dp)

                    )
                }

            }
        }
    }

}
