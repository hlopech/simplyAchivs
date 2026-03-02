package com.example.simplyachivs.domain.usecase.award

import com.example.simplyachivs.domain.model.award.Award
import com.example.simplyachivs.domain.repository.AwardRepository
import javax.inject.Inject

class AddAwardUseCase @Inject constructor(private val awardRepository: AwardRepository) {
    suspend operator fun invoke(award: Award): Result<Unit> = runCatching {
        awardRepository.addAward(award)
    }
}