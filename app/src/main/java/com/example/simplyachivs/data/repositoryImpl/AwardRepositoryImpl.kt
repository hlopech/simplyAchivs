package com.example.simplyachivs.data.repositoryImpl

import com.example.simplyachivs.data.dao.AwardDao
import com.example.simplyachivs.data.mapper.toDomain
import com.example.simplyachivs.data.mapper.toEntity
import com.example.simplyachivs.domain.model.award.Award
import com.example.simplyachivs.domain.repository.AwardRepository
import java.util.UUID
import javax.inject.Inject

class AwardRepositoryImpl @Inject constructor(private val awardDao: AwardDao) : AwardRepository {
    override suspend fun addAward(award: Award) {
        awardDao.insertAward(award.toEntity())
    }

    override suspend fun getAward(awardId: UUID): Award {
        return awardDao.getAward(awardId)!!.toDomain()
    }

    override suspend fun getAllAwards(userId: UUID): List<Award> {
        return awardDao.getAllAwards(userId).map { it.toDomain() }
    }

    override suspend fun getActiveAwards(userId: UUID): List<Award> {
        return awardDao.getActiveAwards(userId).map { it.toDomain() }
    }

    override suspend fun getCompletedAwards(userId: UUID): List<Award> {
        return awardDao.getCompletedAwards(userId).map { it.toDomain() }
    }

    override suspend fun updateAward(award: Award) {
        awardDao.updateAward(award.toEntity())
    }

    override suspend fun deleteAward(award: Award) {
        awardDao.deleteAward(award.toEntity())
    }
}