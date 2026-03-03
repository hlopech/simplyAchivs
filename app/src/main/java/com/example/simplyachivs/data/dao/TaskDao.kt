package com.example.simplyachivs.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.simplyachivs.data.entity.TaskEntity
import com.example.simplyachivs.data.entity.UserEntity
import com.example.simplyachivs.domain.model.task.Task
import java.util.UUID

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity)

    @Delete
    suspend fun deleteTask(task: TaskEntity)

    @Update
    suspend fun updateTask(task: TaskEntity)

    @Query("SELECT * FROM tasks WHERE id = :taskId ")
    suspend fun getTask(taskId: UUID): TaskEntity?


    @Query("SELECT * FROM tasks WHERE userId = :userId ORDER BY completedAt DESC")
    suspend fun getAllTasks(userId: UUID): List<TaskEntity>

    @Query("DELETE FROM tasks WHERE userId = :userId")
    suspend fun deleteAllTasks(userId: UUID)

}