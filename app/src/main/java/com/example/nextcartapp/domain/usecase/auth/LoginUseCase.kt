package com.example.nextcartapp.domain.usecase.auth

import com.example.nextcartapp.core.util.Result
import com.example.nextcartapp.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<String> {
        if (email.isBlank()) return Result.Error(
            com.example.nextcartapp.core.util.AppError.UnknownError("Email non valida")
        )
        if (password.length < 6) return Result.Error(
            com.example.nextcartapp.core.util.AppError.UnknownError("Password troppo corta")
        )
        return authRepository.login(email, password)
    }
}