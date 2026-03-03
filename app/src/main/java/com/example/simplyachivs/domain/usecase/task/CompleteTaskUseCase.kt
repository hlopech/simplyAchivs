package com.example.simplyachivs.domain.usecase.task

import com.example.simplyachivs.domain.model.task.Task
import com.example.simplyachivs.domain.repository.TaskRepository
import com.example.simplyachivs.domain.repository.UserProgressRepository
import com.example.simplyachivs.domain.usecase.progress.GetAwadForTaskUseCase
import java.time.Instant
import java.util.UUID
import javax.inject.Inject

class CompleteTaskUseCase @Inject constructor(
    private val taskRepository: TaskRepository,

    ) {
    suspend operator fun invoke(task: Task): Result<Unit> = runCatching {

        taskRepository.completeTask(
            task.copy(
                completedAt = when (task.completedAt) {
                    null -> Instant.now()
                    else -> null
                }
            )
        )
    }
}