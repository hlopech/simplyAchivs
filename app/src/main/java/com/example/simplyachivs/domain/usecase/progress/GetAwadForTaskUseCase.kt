package com.example.simplyachivs.domain.usecase.progress

import com.example.simplyachivs.domain.model.complexity.TaskComplexity
import com.example.simplyachivs.domain.model.user.UserProgress
import com.example.simplyachivs.domain.repository.UserProgressRepository
import java.time.Instant
import java.util.UUID
import javax.inject.Inject

class GetAwadForTaskUseCase @Inject constructor(
    private val userProgress: UserProgressRepository,
    private val updateStreakUseCase: UpdateStreakUseCase,
) {
    suspend operator fun invoke(
        userId: UUID,
        complexity: TaskComplexity,
        progress: UserProgress
    ): Result<Unit> =
        runCatching {

            updateStreakUseCase(userId, progress)

            val updatedProgress = progress.copy(
                xp = progress.xp + complexity.xp,
                coin = progress.coin + complexity.coins,
                updatedAt = Instant.now()
            )
            userProgress.updateProgress(updatedProgress)
        }

}