package com.example.simplyachivs.domain.usecase.user

import com.example.simplyachivs.domain.model.user.User
import com.example.simplyachivs.domain.repository.UserRepository
import java.util.UUID
import javax.inject.Inject

class LoadUserUseCase @Inject constructor(private val userRepository: UserRepository) {

    suspend operator fun invoke(userId: UUID): Result<User> = runCatching {
        userRepository.getUser(userId)
    }

}