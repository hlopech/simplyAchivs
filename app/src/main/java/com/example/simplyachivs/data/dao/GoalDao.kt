package com.example.simplyachivs.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.simplyachivs.data.entity.GoalEntity
import com.example.simplyachivs.data.entity.StepEntity
import com.example.simplyachivs.domain.model.goal.Goal
import com.example.simplyachivs.domain.model.goal.Step
import java.util.UUID

@Dao
interface GoalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoal(goal: GoalEntity)

    @Delete
    suspend fun deleteGoal(goal: GoalEntity)

    @Update
    suspend fun updateGoal(goal: GoalEntity)

    @Query("SELECT * FROM goals WHERE id = :goalId ")
    suspend fun getGoal(goalId: UUID): GoalEntity?


    @Query("SELECT * FROM goals WHERE userId = :userId ")
    suspend fun getAllGoals(userId: UUID): List<GoalEntity>

    @Query("SELECT * FROM goals WHERE userId = :userId AND status ='ACTIVE'")
    suspend fun getActiveGoals(userId: UUID): List<GoalEntity>

    @Query("SELECT * FROM goals WHERE userId = :userId AND status ='COMPLETED'")
    suspend fun getCompletedGoals(userId: UUID): List<GoalEntity>

}