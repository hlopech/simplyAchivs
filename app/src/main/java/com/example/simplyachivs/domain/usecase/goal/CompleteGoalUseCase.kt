package com.example.simplyachivs.domain.usecase.goal

import com.example.simplyachivs.domain.model.goal.Goal
import com.example.simplyachivs.domain.model.goal.GoalStatus
import com.example.simplyachivs.domain.model.user.UserProgress
import com.example.simplyachivs.domain.repository.GoalRepository
import com.example.simplyachivs.domain.repository.UserProgressRepository
import java.time.Instant
import javax.inject.Inject

class CompleteGoalUseCase @Inject constructor(
    private val goalRepository: GoalRepository,
    private val userProgressRepository: UserProgressRepository,
) {
    suspend operator fun invoke(goal: Goal, progress: UserProgress): Result<Unit> = runCatching {
        goalRepository.updateGoal(
            goal.copy(status = GoalStatus.COMPLETED, completedAt = Instant.now())
        )
        userProgressRepository.updateProgress(
            progress.copy(
                xp = progress.xp + goal.complexity.xp,
                coin = progress.coin + goal.complexity.coins,
                updatedAt = Instant.now()
            )
        )
    }
}
