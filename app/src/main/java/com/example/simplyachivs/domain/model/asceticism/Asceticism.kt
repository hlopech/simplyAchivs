package com.example.simplyachivs.domain.model.asceticism

import java.time.Instant
import java.time.LocalDate
import java.util.UUID

enum class AsceticismStatus { ACTIVE, COMPLETED, ABANDONED }

data class Asceticism(
    val id: UUID,
    val userId: UUID,
    val title: String,
    val description: String,
    val emoji: String,
    val durationDays: Int,
    val startEpochDay: Long,
    val status: AsceticismStatus,
    val createdAt: Instant,
) {
    val startDate: LocalDate get() = LocalDate.ofEpochDay(startEpochDay)
    val endDate: LocalDate get() = startDate.plusDays(durationDays.toLong())
    val daysElapsed: Int
        get() = (LocalDate.now().toEpochDay() - startEpochDay)
            .toInt().coerceIn(0, durationDays)
    val daysRemaining: Int get() = (durationDays - daysElapsed).coerceAtLeast(0)
    val progress: Float get() = daysElapsed.toFloat() / durationDays.coerceAtLeast(1)
    val isExpired: Boolean get() = LocalDate.now() >= endDate
}

data class AsceticismCheckIn(
    val id: UUID,
    val asceticismId: UUID,
    val epochDay: Long,
    val completedAt: Instant,
) {
    val date: LocalDate get() = LocalDate.ofEpochDay(epochDay)
}

data class AsceticismWithProgress(
    val asceticism: Asceticism,
    val checkIns: List<AsceticismCheckIn>,
) {
    val checkedInToday: Boolean
        get() = checkIns.any { it.epochDay == LocalDate.now().toEpochDay() }

    val currentStreak: Int
        get() {
            val dates = checkIns.map { it.epochDay }.toSortedSet()
            var day = if (checkedInToday) LocalDate.now().toEpochDay()
                      else LocalDate.now().toEpochDay() - 1
            var streak = 0
            while (dates.contains(day)) {
                streak++
                day--
            }
            return streak
        }

    val totalCheckedIn: Int get() = checkIns
        .map { it.epochDay }.toSet().size
}
