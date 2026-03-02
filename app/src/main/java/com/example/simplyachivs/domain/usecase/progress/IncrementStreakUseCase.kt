package com.example.simplyachivs.domain.usecase.progress

import com.example.simplyachivs.domain.model.user.UserProgress
import com.example.simplyachivs.domain.repository.UserProgressRepository
import java.time.Instant
import java.time.ZoneId
import java.util.UUID
import javax.inject.Inject

class IncrementStreakUseCase @Inject constructor(private val userProgressRepository: UserProgressRepository) {
    suspend operator fun invoke(userId: UUID, progress: UserProgress): Result<Unit> = runCatching {

        if (progress.updatedAt.atZone(ZoneId.systemDefault()).toLocalDate()
                .plusDays(1) == Instant.now().atZone(ZoneId.systemDefault()).toLocalDate()
        ) {
            userProgressRepository.updateProgress(progress.copy(streak = progress.streak + 1, updatedAt = Instant.now())
            )

        }


    }
}