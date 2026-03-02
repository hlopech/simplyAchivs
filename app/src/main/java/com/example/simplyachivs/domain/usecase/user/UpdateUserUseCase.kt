package com.example.simplyachivs.domain.usecase.user

import com.example.simplyachivs.domain.model.user.User
import com.example.simplyachivs.domain.repository.UserRepository
import javax.inject.Inject

class UpdateUserUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke(user: User): Result<Unit> = runCatching {
        userRepository.updateUser(user)
    }
}