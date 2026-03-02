package com.example.simplyachivs.domain.usecase.task

import com.example.simplyachivs.domain.model.task.Task
import com.example.simplyachivs.domain.repository.TaskRepository
import javax.inject.Inject

class AddTaskUseCase @Inject constructor(private val taskRepository: TaskRepository) {
    suspend operator fun invoke(task: Task): Result<Unit> = runCatching {
        taskRepository.addTask(task)
    }
}