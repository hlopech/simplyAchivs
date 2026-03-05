package com.example.simplyachivs.data.repositoryImpl

import com.example.simplyachivs.data.dao.AsceticismDao
import com.example.simplyachivs.data.entity.toDomain
import com.example.simplyachivs.data.entity.toEntity
import com.example.simplyachivs.domain.model.asceticism.Asceticism
import com.example.simplyachivs.domain.model.asceticism.AsceticismCheckIn
import com.example.simplyachivs.domain.repository.AsceticismRepository
import java.util.UUID
import javax.inject.Inject

class AsceticismRepositoryImpl @Inject constructor(
    private val dao: AsceticismDao,
) : AsceticismRepository {

    override suspend fun create(asceticism: Asceticism) =
        dao.insert(asceticism.toEntity())

    override suspend fun update(asceticism: Asceticism) =
        dao.update(asceticism.toEntity())

    override suspend fun getActive(userId: UUID): List<Asceticism> =
        dao.getActive(userId.toString()).map { it.toDomain() }

    override suspend fun getHistory(userId: UUID): List<Asceticism> =
        dao.getHistory(userId.toString()).map { it.toDomain() }

    override suspend fun getCheckIns(asceticismId: UUID): List<AsceticismCheckIn> =
        dao.getCheckIns(asceticismId.toString()).map { it.toDomain() }

    override suspend fun checkIn(checkIn: AsceticismCheckIn) =
        dao.insertCheckIn(checkIn.toEntity())
}
