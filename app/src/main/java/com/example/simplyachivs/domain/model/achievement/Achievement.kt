package com.example.simplyachivs.domain.model.achievement

data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val isSecret: Boolean,
    val iconRes: Int,
    val status: AchievementStatus,
    val progress: Float,       // 0f..1f
    val progressText: String,  // "3 / 10"
)

enum class AchievementStatus { LOCKED, IN_PROGRESS, COMPLETED }
