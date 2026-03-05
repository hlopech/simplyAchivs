package com.example.simplyachivs.domain.usecase.analytics

import com.example.simplyachivs.domain.model.analytics.AnalyticsData
import com.example.simplyachivs.domain.model.event.EventType
import com.example.simplyachivs.domain.repository.EventRepository
import java.time.LocalDate
import java.time.ZoneId
import java.util.UUID
import javax.inject.Inject

class GetAnalyticsUseCase @Inject constructor(
    private val eventRepository: EventRepository,
) {
    suspend operator fun invoke(userId: UUID): Result<AnalyticsData> = runCatching {
        val events = eventRepository.getAllByUser(userId)
        val zone = ZoneId.systemDefault()

        val completedEvents = events.filter { it.type == EventType.TASK_COMPLETED }

        // Unique completed task IDs (deduplicate toggle spam)
        val uniqueCompletedIds = completedEvents
            .mapNotNull { it.relatedEntityId }
            .toSet()

        // Tasks by day-of-week (unique task IDs per day-of-week bucket)
        val tasksByDayOfWeek = completedEvents
            .groupBy { it.createdAt.atZone(zone).dayOfWeek }
            .mapValues { (_, evts) -> evts.mapNotNull { it.relatedEntityId }.toSet().size }

        // Tasks by hour (unique task IDs per hour bucket)
        val tasksByHour = completedEvents
            .groupBy { it.createdAt.atZone(zone).hour }
            .mapValues { (_, evts) -> evts.mapNotNull { it.relatedEntityId }.toSet().size }

        // Tasks per day — last 14 days
        val today = LocalDate.now(zone)
        val tasksByDate = (0..13)
            .associate { daysAgo ->
                val date = today.minusDays(daysAgo.toLong())
                val count = completedEvents
                    .filter { it.createdAt.atZone(zone).toLocalDate() == date }
                    .mapNotNull { it.relatedEntityId }
                    .toSet().size
                date to count
            }

        // Complexity breakdown
        val complexityBreakdown = completedEvents
            .filter { it.metadata != null }
            .groupBy { it.metadata!! }
            .mapValues { (_, evts) -> evts.mapNotNull { it.relatedEntityId }.toSet().size }

        // Completion rate: unique completed vs unique uncompleted (different task IDs)
        val uncompletedCount = events
            .filter { it.type == EventType.TASK_UNCOMPLETED }
            .mapNotNull { it.relatedEntityId }
            .toSet().size
        val completionRate =
            if (uniqueCompletedIds.isEmpty() && uncompletedCount == 0) 1f
            else uniqueCompletedIds.size.toFloat() /
                    (uniqueCompletedIds.size + uncompletedCount).coerceAtLeast(1)

        val goalsCompleted = events
            .filter { it.type == EventType.GOAL_COMPLETED }
            .mapNotNull { it.relatedEntityId }.toSet().size
        val stepsCompleted = events
            .filter { it.type == EventType.STEP_COMPLETED }
            .mapNotNull { it.relatedEntityId }.toSet().size

        AnalyticsData(
            totalTasksCompleted = uniqueCompletedIds.size,
            totalGoalsCompleted = goalsCompleted,
            totalStepsCompleted = stepsCompleted,
            tasksByDayOfWeek = tasksByDayOfWeek,
            tasksByHour = tasksByHour,
            tasksByDate = tasksByDate,
            complexityBreakdown = complexityBreakdown,
            completionRate = completionRate,
        )
    }
}
