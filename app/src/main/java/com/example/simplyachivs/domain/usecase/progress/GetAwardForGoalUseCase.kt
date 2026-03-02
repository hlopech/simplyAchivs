package com.example.simplyachivs.domain.usecase.progress

import com.example.simplyachivs.domain.model.complexity.GoalComplexity
import com.example.simplyachivs.domain.model.complexity.TaskComplexity
import com.example.simplyachivs.domain.model.goal.Goal
import com.example.simplyachivs.domain.model.user.UserProgress
import com.example.simplyachivs.domain.repository.UserProgressRepository
import java.time.Instant
import java.util.UUID
import javax.inject.Inject

class GetAwardForGoalUseCase @Inject constructor(private val userProgress: UserProgressRepository) {
    suspend operator fun invoke(
        userId: UUID,
        complexity: GoalComplexity,
        progress: UserProgress
    ): Result<Unit> =
        runCatching {
            val updatedProgress = progress.copy(
                xp = progress.xp + complexity.xp,
                coin = progress.coin + complexity.coins,
                updatedAt = Instant.now()
            )
            userProgress.updateProgress( updatedProgress)
        }

}