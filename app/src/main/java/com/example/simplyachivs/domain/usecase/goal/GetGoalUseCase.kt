package com.example.simplyachivs.domain.usecase.goal

import com.example.simplyachivs.domain.model.goal.Goal
import com.example.simplyachivs.domain.repository.GoalRepository
import java.util.UUID
import javax.inject.Inject

class GetGoalUseCase @Inject constructor(private val goalRepository: GoalRepository) {
    suspend operator fun invoke(goalId: UUID): Result<Goal> = runCatching {
        goalRepository.getGoal(goalId)
    }
}