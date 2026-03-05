package com.example.simplyachivs.domain.repository

import com.example.simplyachivs.domain.model.goal.Goal
import com.example.simplyachivs.domain.model.goal.Step
import java.util.UUID

interface GoalRepository {
    suspend fun addGoal(goal: Goal)
    suspend fun getGoal(goalId: UUID): Goal
    suspend fun getAllGoals(userId: UUID):List<Goal>
    suspend fun getActiveGoals(userId: UUID):List<Goal>
    suspend fun getCompletedGoals(userId: UUID):List<Goal>
    suspend fun deleteGoal(goal: Goal)
    suspend fun updateGoal(goal: Goal)
    suspend fun addStep(step: Step)
    suspend fun getSteps(goalId: UUID):List<Step>
    suspend fun updateStep(step: Step)

}