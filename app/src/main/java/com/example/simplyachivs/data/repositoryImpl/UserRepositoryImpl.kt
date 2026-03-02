package com.example.simplyachivs.data.repositoryImpl

import com.example.simplyachivs.data.dao.UserDao
import com.example.simplyachivs.data.mapper.toDomain
import com.example.simplyachivs.data.mapper.toEntity
import com.example.simplyachivs.domain.model.user.User
import com.example.simplyachivs.domain.repository.UserRepository
import java.util.UUID
import javax.inject.Inject


class UserRepositoryImpl @Inject constructor(private val userDao: UserDao) : UserRepository {
    override suspend fun createUser(user: User) {
        userDao.insertUser(user.toEntity())
    }

    override suspend fun updateUser(newUser: User) {
        userDao.updateUser(newUser.toEntity())
    }

    override suspend fun getUser(userId: UUID): User {
        return userDao.getUser(userId)!!.toDomain()
    }


}