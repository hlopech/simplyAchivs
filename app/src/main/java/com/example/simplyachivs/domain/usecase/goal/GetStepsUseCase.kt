package com.example.simplyachivs.domain.usecase.goal

import com.example.simplyachivs.domain.model.goal.Step
import com.example.simplyachivs.domain.repository.GoalRepository
import java.util.UUID
import javax.inject.Inject

class GetStepsUseCase @Inject constructor(private val goalRepository: GoalRepository) {
    suspend operator fun invoke(goalId: UUID): Result<List<Step>> = runCatching {
        goalRepository.getSteps(goalId).sortedBy { it.position }
    }
}
