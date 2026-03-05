package com.example.simplyachivs.presentation.achievements

import androidx.lifecycle.ViewModel
import com.example.simplyachivs.domain.service.AchievementNotifier
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GlobalAchievementViewModel @Inject constructor(
    achievementNotifier: AchievementNotifier,
) : ViewModel() {
    val newAchievements = achievementNotifier.events
}
