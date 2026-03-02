package com.example.simplyachivs.domain.usecase.task

import com.example.simplyachivs.domain.model.task.Task
import com.example.simplyachivs.domain.repository.TaskRepository
import java.util.UUID
import javax.inject.Inject

class GetTasksUseCase @Inject constructor(private val taskRepository: TaskRepository) {
    suspend operator fun invoke(userId: UUID): Result<List<Task>> = runCatching{
        taskRepository.getTasks(userId)
    }
}