package com.example.simplyachivs.domain.usecase.award

import com.example.simplyachivs.domain.model.award.Award
import com.example.simplyachivs.domain.model.award.AwardStatus
import com.example.simplyachivs.domain.repository.AwardRepository
import java.time.Instant
import javax.inject.Inject

class CompleteAwardUseCase @Inject constructor(private val awardRepository: AwardRepository) {
    suspend operator fun invoke(award: Award): Result<Unit> = runCatching {
        awardRepository.updateAward(award.copy(status = AwardStatus.COMPLETED, completedAt = Instant.now()))
    }
}