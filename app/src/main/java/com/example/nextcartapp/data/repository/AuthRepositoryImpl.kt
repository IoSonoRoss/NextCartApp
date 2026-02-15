package com.example.nextcartapp.data.repository

import com.example.nextcartapp.core.session.SessionManager
import com.example.nextcartapp.core.util.AppError
import com.example.nextcartapp.core.util.Result
import com.example.nextcartapp.data.remote.api.AuthApi
import com.example.nextcartapp.data.remote.dto.LoginRequestDto
import com.example.nextcartapp.data.remote.dto.RegisterRequestDto
import com.example.nextcartapp.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val sessionManager: SessionManager
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<String> {
        return try {
            val response = authApi.login(LoginRequestDto(email, password))
            if (response.isSuccessful) {
                val token = response.body()?.accessToken
                if (token != null) {
                    sessionManager.saveAccessToken(token)
                    Result.Success(token)
                } else {
                    Result.Error(AppError.UnknownError("Token non ricevuto"))
                }
            } else {
                when (response.code()) {
                    401 -> Result.Error(AppError.UnauthorizedError("Credenziali non valide"))
                    else -> Result.Error(AppError.ServerError(response.code(), "Errore server"))
                }
            }
        } catch (e: Exception) {
            Result.Error(AppError.NetworkError(e.message ?: "Errore di rete"))
        }
    }

    override suspend fun register(
        email: String,
        password: String,
        name: String,
        surname: String?,
        dateOfBirth: String,
        gender: String,
        address: String?
    ): Result<Int> {
        return try {
            val response = authApi.register(
                RegisterRequestDto(email, password, name, surname, dateOfBirth, gender, address)
            )
            if (response.isSuccessful) {
                val userId = response.body()?.userId
                if (userId != null) Result.Success(userId)
                else Result.Error(AppError.UnknownError("UserId non ricevuto"))
            } else {
                Result.Error(AppError.ServerError(response.code(), "Errore registrazione"))
            }
        } catch (e: Exception) {
            Result.Error(AppError.NetworkError(e.message ?: "Errore di rete"))
        }
    }

    override suspend fun logout(): Result<Unit> {
        return try {
            authApi.logout()
            sessionManager.clearSession()
            Result.Success(Unit)
        } catch (e: Exception) {
            sessionManager.clearSession()
            Result.Success(Unit)
        }
    }
}