package com.example.simplyachivs.domain.repository

import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface SessionRepository {
    val userId: Flow<UUID?>
    val lastResetDate: Flow<String?>
    suspend fun saveUserId(userId: UUID)
    suspend fun saveLastResetDate(date: String)
    suspend fun clearSession()
}
