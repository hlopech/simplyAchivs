package com.example.simplyachivs.domain.usecase.progress

import com.example.simplyachivs.domain.model.complexity.TaskComplexity
import com.example.simplyachivs.domain.model.user.UserProgress
import com.example.simplyachivs.domain.repository.UserProgressRepository
import java.time.Instant
import javax.inject.Inject

class RevokeTaskRewardUseCase @Inject constructor(
    private val userProgressRepository: UserProgressRepository,
) {
    suspend operator fun invoke(complexity: TaskComplexity, progress: UserProgress): Result<Unit> = runCatching {
        userProgressRepository.updateProgress(
            progress.copy(
                xp = (progress.xp - complexity.xp).coerceAtLeast(0),
                coin = (progress.coin - complexity.coins).coerceAtLeast(0),
                updatedAt = Instant.now()
            )
        )
    }
}
