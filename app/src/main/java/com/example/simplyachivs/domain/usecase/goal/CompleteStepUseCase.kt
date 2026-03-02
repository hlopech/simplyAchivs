package com.example.simplyachivs.domain.usecase.goal

import com.example.simplyachivs.domain.model.goal.Step
import com.example.simplyachivs.domain.model.goal.StepStatus
import com.example.simplyachivs.domain.model.goal.StepStatus.*
import com.example.simplyachivs.domain.repository.GoalRepository
import javax.inject.Inject

class CompleteStepUseCase @Inject constructor(private val goalRepository: GoalRepository) {
    suspend operator fun invoke(step: Step): Result<Unit> = runCatching {
        when (step.status) {
            ACTIVE -> goalRepository.updateStep(step.copy(status = COMPLETED))

            COMPLETED -> goalRepository.updateStep(step.copy(status = ACTIVE))
        }
    }
}