package com.example.simplyachivs.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.simplyachivs.data.entity.StepEntity
import com.example.simplyachivs.data.entity.TaskEntity
import com.example.simplyachivs.domain.model.goal.Step
import java.util.UUID

@Dao
interface StepDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStep(step: StepEntity)

    @Delete
    suspend fun deleteStep(step: StepEntity)

    @Update
    suspend fun updateStep(step: StepEntity)

    @Query("SELECT * FROM steps WHERE id = :stepId ")
    suspend fun getStep(stepId: UUID): StepEntity?


    @Query("SELECT * FROM steps WHERE goalId = :goalId ")
    suspend fun getAllSteps(goalId: UUID): List<StepEntity>

}