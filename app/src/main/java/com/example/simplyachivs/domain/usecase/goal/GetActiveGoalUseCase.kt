package com.example.simplyachivs.domain.usecase.goal

import com.example.simplyachivs.domain.model.goal.Goal
import com.example.simplyachivs.domain.repository.GoalRepository
import java.util.UUID
import javax.inject.Inject

class GetActiveGoalUseCase @Inject constructor(private val goalRepository: GoalRepository) {

    suspend operator fun invoke(userId: UUID): Result<List<Goal>> = runCatching {
        goalRepository.getActiveGoals(userId)
    }
}