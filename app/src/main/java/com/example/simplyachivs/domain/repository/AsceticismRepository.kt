package com.example.simplyachivs.domain.repository

import com.example.simplyachivs.domain.model.asceticism.Asceticism
import com.example.simplyachivs.domain.model.asceticism.AsceticismCheckIn
import java.util.UUID

interface AsceticismRepository {
    suspend fun create(asceticism: Asceticism)
    suspend fun update(asceticism: Asceticism)
    suspend fun getActive(userId: UUID): List<Asceticism>
    suspend fun getHistory(userId: UUID): List<Asceticism>
    suspend fun getCheckIns(asceticismId: UUID): List<AsceticismCheckIn>
    suspend fun checkIn(checkIn: AsceticismCheckIn)
}
