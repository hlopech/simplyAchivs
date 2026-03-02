package com.example.simplyachivs.domain.usecase.award

import com.example.simplyachivs.domain.model.award.Award
import com.example.simplyachivs.domain.repository.AwardRepository
import java.util.UUID
import javax.inject.Inject

class GetAwardUseCase @Inject constructor(private val awardRepository: AwardRepository) {
    suspend operator fun invoke(awardId: UUID): Result<Award> = runCatching {
        awardRepository.getAward(awardId)
    }
}