package com.example.simplyachivs.domain.repository

import com.example.simplyachivs.domain.model.award.Award
import java.util.UUID

interface AwardRepository {
    suspend fun addAward(award: Award)
    suspend fun getAward(awardId: UUID): Award
    suspend fun getAllAwards(userId: UUID): List<Award>
    suspend fun getActiveAwards(userId: UUID): List<Award>
    suspend fun getCompletedAwards(userId: UUID): List<Award>
    suspend fun updateAward(award: Award)
    suspend fun deleteAward(award: Award)
}