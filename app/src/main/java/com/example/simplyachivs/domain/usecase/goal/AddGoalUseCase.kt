package com.example.simplyachivs.domain.usecase.goal

import com.example.simplyachivs.domain.model.goal.Goal
import com.example.simplyachivs.domain.repository.GoalRepository
import javax.inject.Inject

class AddGoalUseCase @Inject constructor(private val goalRepository: GoalRepository) {

    suspend operator fun invoke(goal: Goal): Result<Unit> = runCatching {
        goalRepository.addGoal(goal)
    }
}