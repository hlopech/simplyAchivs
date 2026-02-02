package com.example.simplyachivs.domain.model.user

import java.time.Instant
import java.util.UUID

data class UserProgress(
    val id: UUID,
    val userId: UUID,
    val xp: Int,
    val coin: Int,
    val streak: Int,
    val updatedAt: Instant
)
