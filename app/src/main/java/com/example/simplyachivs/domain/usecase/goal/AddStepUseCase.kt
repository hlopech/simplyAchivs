package com.example.simplyachivs.domain.usecase.goal

import com.example.simplyachivs.domain.model.goal.Step
import com.example.simplyachivs.domain.repository.GoalRepository
import javax.inject.Inject

class AddStepUseCase @Inject constructor(private val goalRepository: GoalRepository) {
    suspend operator fun invoke(step: Step): Result<Unit> = runCatching {
        goalRepository.addStep(step)
    }
}
