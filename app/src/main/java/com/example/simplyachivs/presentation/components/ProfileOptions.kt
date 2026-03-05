package com.example.simplyachivs.presentation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.simplyachivs.R

@Composable
fun ProfileOptions(
    onSettingsClick: () -> Unit,
    onAchievementsClick: () -> Unit,
    onAnalyticsClick: () -> Unit,
    onAsceticismClick: () -> Unit,
) {
    LazyColumn(
        contentPadding = PaddingValues(vertical = 10.dp),
        modifier = Modifier.padding(vertical = 0.dp)
    ) {
        item {
            ProfileOption(
                text = "Достижения",
                onClick = onAchievementsClick,
                icon = R.drawable.trophy_icon
            )
        }
        item {
            ProfileOption(
                text = "Аналитика",
                onClick = onAnalyticsClick,
                icon = R.drawable.chart_icon
            )
        }
        item {
            ProfileOption(
                text = "Аскеза",
                onClick = onAsceticismClick,
                icon = R.drawable.fire_icon
            )
        }
        item {
            ProfileOption(
                text = "Настройки",
                onClick = onSettingsClick,
                icon = R.drawable.setting_icon
            )
        }
    }
}
