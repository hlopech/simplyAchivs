package com.example.simplyachivs.data.repositoryImpl

import com.example.simplyachivs.data.dao.TaskDao
import com.example.simplyachivs.data.mapper.toDomain
import com.example.simplyachivs.data.mapper.toEntity
import com.example.simplyachivs.domain.model.task.Task
import com.example.simplyachivs.domain.repository.TaskRepository
import java.util.UUID
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(private val taskDao: TaskDao) : TaskRepository {
    override suspend fun addTask(task: Task) {
        taskDao.insertTask(task.toEntity())
    }

    override suspend fun getTasks(userId: UUID): List<Task> {
        return taskDao.getAllTasks(userId).map { it.toDomain() }
    }

    override suspend fun completeTask(task: Task) {
        taskDao.updateTask(task.toEntity())
    }

    override suspend fun deleteAllTasks(userId: UUID) {
        taskDao.deleteAllTasks(userId)
    }
}