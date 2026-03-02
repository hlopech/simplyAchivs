package com.example.simplyachivs.domain.repository

import com.example.simplyachivs.domain.model.user.User
import java.util.UUID

interface UserRepository {
    suspend fun createUser(user: User)
    suspend fun updateUser(newUser: User)
    suspend fun getUser(userId: UUID): User
}