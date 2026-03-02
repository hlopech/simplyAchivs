package com.example.simplyachivs.data.repositoryImpl

import com.example.simplyachivs.data.dao.UserProgressDao
import com.example.simplyachivs.data.mapper.toDomain
import com.example.simplyachivs.data.mapper.toEntity
import com.example.simplyachivs.domain.model.user.UserProgress
import com.example.simplyachivs.domain.repository.UserProgressRepository
import java.util.UUID
import javax.inject.Inject

class UserProgressRepositoryImpl @Inject constructor(private val userProgressDao: UserProgressDao) :
    UserProgressRepository {
    override suspend fun getProgress(userId: UUID): UserProgress {
        return userProgressDao.getUserProgress(userId)!!.toDomain()
    }
    override suspend fun updateProgress(
        progress: UserProgress
    ) {
        userProgressDao.updateUserProgress(progress.toEntity())
    }
    override suspend fun addProgress(progress: UserProgress) {
        userProgressDao.insertUserProgress(progress.toEntity())

    }

}