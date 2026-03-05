package com.example.simplyachivs.domain.usecase.achievement

import com.example.simplyachivs.R
import com.example.simplyachivs.domain.model.achievement.Achievement
import com.example.simplyachivs.domain.model.achievement.AchievementStatus
import com.example.simplyachivs.domain.model.event.Event
import com.example.simplyachivs.domain.model.event.EventType
import com.example.simplyachivs.domain.repository.EventRepository
import java.time.ZoneId
import java.util.UUID
import javax.inject.Inject

class GetAchievementsUseCase @Inject constructor(
    private val eventRepository: EventRepository,
) {
    suspend operator fun invoke(userId: UUID): Result<List<Achievement>> = runCatching {
        val events = eventRepository.getAllByUser(userId)
        buildAchievements(events)
    }

    private fun buildAchievements(events: List<Event>): List<Achievement> {
        // For task/step/goal completions — use distinct relatedEntityId to avoid
        // counting the same entity multiple times when toggled repeatedly.
        val completedTaskIds = events
            .filter { it.type == EventType.TASK_COMPLETED }
            .mapNotNull { it.relatedEntityId }
            .toSet()

        val taskCompleted   = completedTaskIds.size
        val taskUncompleted = events.count { it.type == EventType.TASK_UNCOMPLETED }
        val goalCompleted   = events
            .filter { it.type == EventType.GOAL_COMPLETED }
            .mapNotNull { it.relatedEntityId }.toSet().size
        val goalCreated     = events
            .filter { it.type == EventType.GOAL_CREATED }
            .mapNotNull { it.relatedEntityId }.toSet().size
        val stepCompleted   = events
            .filter { it.type == EventType.STEP_COMPLETED }
            .mapNotNull { it.relatedEntityId }.toSet().size
        val awardPurchased  = events.count { it.type == EventType.AWARD_PURCHASED }

        val hardTaskDone = events.any {
            it.type == EventType.TASK_COMPLETED &&
                it.relatedEntityId in completedTaskIds &&
                it.metadata?.contains("HARD") == true
        }
        val maxTasksInDay = events
            .filter { it.type == EventType.TASK_COMPLETED }
            .groupBy { it.createdAt.atZone(ZoneId.systemDefault()).toLocalDate() }
            .values.maxOfOrNull { group ->
                group.mapNotNull { it.relatedEntityId }.toSet().size
            } ?: 0

        return listOf(
            achievement(
                id = "first_task",
                title = "Первый шаг",
                description = "Выполните свою первую задачу",
                icon = R.drawable.star_icon,
                current = taskCompleted,
                target = 1,
            ),
            achievement(
                id = "task_master",
                title = "Трудяга",
                description = "Выполните 10 задач",
                icon = R.drawable.fire_icon,
                current = taskCompleted,
                target = 10,
            ),
            achievement(
                id = "first_goal",
                title = "Цель достигнута",
                description = "Завершите свою первую цель",
                icon = R.drawable.flag_icon,
                current = goalCompleted,
                target = 1,
            ),
            achievement(
                id = "goal_conqueror",
                title = "Покоритель целей",
                description = "Завершите 3 цели",
                icon = R.drawable.target_icon,
                current = goalCompleted,
                target = 3,
            ),
            achievement(
                id = "architect",
                title = "Архитектор",
                description = "Создайте свою первую цель",
                icon = R.drawable.draw_icon,
                current = goalCreated,
                target = 1,
            ),
            achievement(
                id = "shopaholic",
                title = "Шопоголик",
                description = "Приобретите 3 награды в магазине",
                icon = R.drawable.shop_icon,
                current = awardPurchased,
                target = 3,
            ),
            achievement(
                id = "perfectionist",
                title = "Перфекционист",
                description = "Выполните 20 шагов целей",
                icon = R.drawable.complexity_icon,
                current = stepCompleted,
                target = 20,
            ),
            // Secret achievements
            achievement(
                id = "hard_worker",
                title = "Железная воля",
                description = "Выполните задачу с максимальной сложностью",
                isSecret = true,
                icon = R.drawable.complexity_icon,
                current = if (hardTaskDone) 1 else 0,
                target = 1,
            ),
            achievement(
                id = "unstoppable",
                title = "Неудержимый",
                description = "Выполните 5 задач за один день",
                isSecret = true,
                icon = R.drawable.fire_icon,
                current = maxTasksInDay,
                target = 5,
            ),
            achievement(
                id = "indecisive",
                title = "Сомневающийся",
                description = "Снимите галочку с задачи 3 раза",
                isSecret = true,
                icon = R.drawable.star_icon,
                current = taskUncompleted,
                target = 3,
            ),
        )
    }

    private fun achievement(
        id: String,
        title: String,
        description: String,
        isSecret: Boolean = false,
        icon: Int,
        current: Int,
        target: Int,
    ): Achievement {
        val clamped = current.coerceAtMost(target)
        val status = when {
            clamped >= target -> AchievementStatus.COMPLETED
            clamped > 0       -> AchievementStatus.IN_PROGRESS
            else              -> AchievementStatus.LOCKED
        }
        return Achievement(
            id = id,
            title = if (isSecret && status != AchievementStatus.COMPLETED) "???" else title,
            description = if (isSecret && status != AchievementStatus.COMPLETED) "Секретное достижение. Продолжайте играть, чтобы открыть его!" else description,
            isSecret = isSecret,
            iconRes = icon,
            status = status,
            progress = clamped.toFloat() / target,
            progressText = "$clamped / $target",
        )
    }
}
