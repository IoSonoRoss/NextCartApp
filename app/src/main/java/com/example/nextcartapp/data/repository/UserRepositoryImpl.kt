package com.example.nextcartapp.data.repository

import com.example.nextcartapp.core.util.AppError
import com.example.nextcartapp.core.util.Result
import com.example.nextcartapp.data.remote.api.ProfileApi
import com.example.nextcartapp.domain.model.User
import com.example.nextcartapp.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val profileApi: ProfileApi
) : UserRepository {

    override suspend fun getProfile(): Result<User> {
        return try {
            val response = profileApi.getProfile()

            if (response.isSuccessful) {
                val dto = response.body()

                if (dto != null) {
                    val user = User(
                        userId = dto.consumerId,
                        email = dto.email,
                        name = dto.name,
                        surname = dto.surname,
                        gender = dto.gender ?: "",
                        dateOfBirth = dto.dateOfBirth ?: "",
                        address = dto.address
                    )
                    Result.Success(user)
                } else {
                    Result.Error(AppError.UnknownError("Profile data not found"))
                }
            } else {
                Result.Error(AppError.ServerError(response.code(), "Failed to load profile"))
            }
        } catch (e: Exception) {
            Result.Error(AppError.NetworkError(e.message ?: "Network error"))
        }
    }

    override suspend fun updateProfile(user: User): Result<User> {
        // TODO: Implementare quando serve
        return Result.Error(AppError.UnknownError("Not implemented yet"))
    }
}