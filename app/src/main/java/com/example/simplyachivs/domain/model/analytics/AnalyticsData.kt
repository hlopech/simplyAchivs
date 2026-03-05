package com.example.simplyachivs.domain.model.analytics

import java.time.DayOfWeek
import java.time.LocalDate

data class AnalyticsData(
    val totalTasksCompleted: Int,
    val totalGoalsCompleted: Int,
    val totalStepsCompleted: Int,
    /** Unique tasks completed per day-of-week (Mon..Sun) */
    val tasksByDayOfWeek: Map<DayOfWeek, Int>,
    /** Unique tasks completed per hour (0..23) */
    val tasksByHour: Map<Int, Int>,
    /** Unique tasks completed per date — last 14 days, newest first */
    val tasksByDate: Map<LocalDate, Int>,
    /** "EASY" / "MEDIUM" / "HARD" -> count */
    val complexityBreakdown: Map<String, Int>,
    /** fraction 0..1: uniqueCompleted / (uniqueCompleted + uncompleted) */
    val completionRate: Float,
)
