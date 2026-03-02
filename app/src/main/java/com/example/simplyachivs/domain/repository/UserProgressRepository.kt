package com.example.simplyachivs.domain.repository

import com.example.simplyachivs.domain.model.user.UserProgress
import java.util.UUID

interface UserProgressRepository {
    suspend fun getProgress(userId: UUID): UserProgress
    suspend fun updateProgress(progress: UserProgress)
    suspend fun addProgress(progress: UserProgress)

}