package com.example.simplyachivs.domain.model.user

import java.time.Instant
import java.util.UUID

data class User(
    val id: UUID,
    val name: String,
    val avatar: String,
    val createdAt: Instant,
    val lastActiveAt: Instant
)
