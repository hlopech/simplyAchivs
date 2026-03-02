package com.example.simplyachivs.domain.usecase.progress

import com.example.simplyachivs.domain.model.user.UserProgress
import com.example.simplyachivs.domain.repository.UserProgressRepository
import java.time.Instant
import java.util.UUID
import javax.inject.Inject

class CreateUserProgressUseCase @Inject constructor(private val userProgressRepository: UserProgressRepository) {
    suspend operator fun invoke(userId: UUID): Result<Unit> = runCatching {
        userProgressRepository.addProgress(
            UserProgress(
                UUID.randomUUID(),
                userId,
                0,
                0,
                0,
                Instant.now()
            )
        )
    }
}