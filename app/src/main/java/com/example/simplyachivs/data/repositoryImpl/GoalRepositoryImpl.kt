package com.example.simplyachivs.data.repositoryImpl

import com.example.simplyachivs.data.dao.GoalDao
import com.example.simplyachivs.data.dao.StepDao
import com.example.simplyachivs.data.mapper.toDomain
import com.example.simplyachivs.data.mapper.toEntity
import com.example.simplyachivs.domain.model.goal.Goal
import com.example.simplyachivs.domain.model.goal.Step
import com.example.simplyachivs.domain.repository.GoalRepository
import java.util.UUID
import javax.inject.Inject

class GoalRepositoryImpl @Inject constructor(private val goalDao: GoalDao, private val stepDao: StepDao) :
    GoalRepository {
    override suspend fun addGoal(goal: Goal) {
        goalDao.insertGoal(goal.toEntity())
    }

    override suspend fun getGoal(goalId: UUID): Goal {
        return goalDao.getGoal(goalId)!!.toDomain()
    }

    override suspend fun getAllGoals(userId: UUID): List<Goal> {
        return goalDao.getAllGoals(userId).map { it.toDomain() }
    }

    override suspend fun getActiveGoals(userId: UUID): List<Goal> {
        return goalDao.getActiveGoals(userId).map { it.toDomain() }
    }

    override suspend fun getCompletedGoals(userId: UUID): List<Goal> {
        return goalDao.getCompletedGoals(userId).map { it.toDomain() }
    }

    override suspend fun updateGoal(goal: Goal) {
        goalDao.updateGoal(goal.toEntity())
    }

    override suspend fun addStep(step: Step) {
        stepDao.insertStep(step.toEntity())
    }

    override suspend fun getSteps(goalId: UUID): List<Step> {
        return stepDao.getAllSteps(goalId).map { it.toDomain() }
    }

    override suspend fun updateStep(step: Step) {
        stepDao.updateStep(step.toEntity())
    }
}