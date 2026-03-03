package com.example.simplyachivs.domain.repository

import com.example.simplyachivs.domain.model.task.Task
import java.util.UUID

interface TaskRepository {
    suspend fun addTask(task: Task)
    suspend fun getTasks(userId: UUID): List<Task>
    suspend fun completeTask(task: Task)
    suspend fun deleteAllTasks(userId: UUID)
}