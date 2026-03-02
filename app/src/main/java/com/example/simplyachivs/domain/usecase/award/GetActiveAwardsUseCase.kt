package com.example.simplyachivs.domain.usecase.award

import com.example.simplyachivs.domain.model.award.Award
import com.example.simplyachivs.domain.repository.AwardRepository
import java.util.UUID
import javax.inject.Inject

class GetActiveAwardsUseCase @Inject constructor(private val awardRepository: AwardRepository) {
    suspend operator fun invoke(userId: UUID): Result<List<Award>> = runCatching {
        awardRepository.getActiveAwards(userId)
    }
}