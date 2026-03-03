package com.example.simplyachivs.domain.usecase.task

import com.example.simplyachivs.domain.repository.SessionRepository
import com.example.simplyachivs.domain.repository.TaskRepository
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import java.util.UUID
import javax.inject.Inject

class ResetDailyTasksUseCase @Inject constructor(
    private val taskRepository: TaskRepository,
    private val sessionRepository: SessionRepository
) {
    suspend operator fun invoke(userId: UUID): Result<Boolean> = runCatching {
        val today = LocalDate.now().toString()
        val lastReset = sessionRepository.lastResetDate.first()

        if (lastReset == today) return@runCatching false

        taskRepository.deleteAllTasks(userId)
        sessionRepository.saveLastResetDate(today)
        true
    }
}
