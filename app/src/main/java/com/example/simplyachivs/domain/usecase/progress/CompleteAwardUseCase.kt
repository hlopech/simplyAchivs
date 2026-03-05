package com.example.simplyachivs.domain.usecase.progress

import com.example.simplyachivs.domain.model.award.Award
import com.example.simplyachivs.domain.model.award.AwardStatus
import com.example.simplyachivs.domain.model.user.UserProgress
import com.example.simplyachivs.domain.repository.AwardRepository
import com.example.simplyachivs.domain.repository.UserProgressRepository
import kotlinx.coroutines.withContext
import java.time.Instant
import java.util.UUID
import javax.inject.Inject

class CompleteAwardUseCase @Inject constructor(
    private val userProgressRepository: UserProgressRepository,
    private val awardRepository: AwardRepository
) {
    suspend operator fun invoke(
        userId: UUID,
        progress: UserProgress,
        price: Int,
        award: Award
    ): Result<Unit> =
        runCatching {
            if (progress.coin - price >= 0) {
                userProgressRepository.updateProgress(
                    progress = progress.copy(coin = progress.coin - price)
                )
                awardRepository.updateAward(
                    award.copy(
                        status = AwardStatus.COMPLETED,
                        completedAt = Instant.now()
                    )
                )
            } else {
                throw Exception("У вас недостаточно монет")
            }
        }
}