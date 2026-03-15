package com.example.nextcartapp.domain.usecase.profile

import com.example.nextcartapp.domain.model.User
import com.example.nextcartapp.domain.repository.UserRepository
import com.example.nextcartapp.core.util.Result
import javax.inject.Inject

class GetUserProfileUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Result<User> {
        return userRepository.getProfile()
    }
}