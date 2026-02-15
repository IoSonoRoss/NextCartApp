package com.example.nextcartapp.domain.usecase.auth

import com.example.nextcartapp.core.util.Result
import com.example.nextcartapp.domain.repository.AuthRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return authRepository.logout()
    }
}