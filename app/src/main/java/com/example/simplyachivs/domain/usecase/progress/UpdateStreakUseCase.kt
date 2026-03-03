package com.example.simplyachivs.domain.usecase.progress

import com.example.simplyachivs.domain.model.user.UserProgress
import com.example.simplyachivs.domain.repository.UserProgressRepository
import java.time.Instant
import java.time.ZoneId
import java.util.UUID
import javax.inject.Inject

class UpdateStreakUseCase @Inject constructor(private val userProgressRepository: UserProgressRepository) {
    suspend operator fun invoke(userId: UUID, progress: UserProgress): Result<Unit> = runCatching {
        val zone = ZoneId.systemDefault()
        val today = Instant.now().atZone(zone).toLocalDate()
        val last = progress.updatedAt.atZone(zone).toLocalDate()

        val newProgress = when (last) {
            today -> progress
            today.minusDays(1) -> progress.copy(
                streak = progress.streak + 1,
                updatedAt = Instant.now()
            )
            else -> progress.copy(
                streak = 1,
                updatedAt = Instant.now()
            )
        }
        userProgressRepository.updateProgress(newProgress)
    }
    
}