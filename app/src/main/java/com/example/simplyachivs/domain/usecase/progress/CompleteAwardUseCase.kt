package com.example.simplyachivs.domain.usecase.progress

import com.example.simplyachivs.domain.model.user.UserProgress
import com.example.simplyachivs.domain.repository.UserProgressRepository
import java.util.UUID
import javax.inject.Inject

class CompleteAwardUseCase @Inject constructor(private val userProgressRepository: UserProgressRepository) {
    suspend operator fun invoke(userId: UUID, progress: UserProgress, price: Int): Result<Unit> =
        runCatching {
            userProgressRepository.updateProgress(
                progress = progress.copy(coin = progress.coin - price)
            )
        }
}