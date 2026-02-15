package com.example.nextcartapp.domain.usecase.auth

import com.example.nextcartapp.core.util.AppError
import com.example.nextcartapp.core.util.Result
import com.example.nextcartapp.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String,
        name: String,
        surname: String?,
        dateOfBirth: String,
        gender: String,
        address: String?
    ): Result<Int> {
        if (email.isBlank()) return Result.Error(AppError.UnknownError("Email non valida"))
        if (password.length < 6) return Result.Error(AppError.UnknownError("Password troppo corta"))
        if (name.isBlank()) return Result.Error(AppError.UnknownError("Nome non valido"))
        if (dateOfBirth.isBlank()) return Result.Error(AppError.UnknownError("Data di nascita non valida"))
        return authRepository.register(email, password, name, surname, dateOfBirth, gender, address)
    }
}