package com.example.nextcartapp.data.repository

import com.example.nextcartapp.core.util.AppError
import com.example.nextcartapp.core.util.Result
import com.example.nextcartapp.data.remote.api.HealthConditionApi
import com.example.nextcartapp.data.remote.dto.FilterHealthConditionsDto
import com.example.nextcartapp.data.remote.dto.UpdateHealthConditionsDto
import com.example.nextcartapp.domain.model.HealthCategory
import com.example.nextcartapp.domain.model.HealthCondition
import com.example.nextcartapp.domain.repository.HealthConditionRepository
import javax.inject.Inject

class HealthConditionRepositoryImpl @Inject constructor(
    private val api: HealthConditionApi
) : HealthConditionRepository {

    override suspend fun getCategories(): Result<List<HealthCategory>> {
        return try {
            val response = api.getCategories()

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    val categories = body.value.map { dto ->
                        HealthCategory(
                            id = dto.id,
                            code = dto.code,
                            label = dto.label
                        )
                    }
                    Result.Success(categories)
                } else {
                    Result.Error(AppError.UnknownError("Response body is null"))
                }
            } else {
                Result.Error(AppError.ServerError(response.code(), "Failed to load categories"))
            }
        } catch (e: Exception) {
            Result.Error(AppError.NetworkError(e.message ?: "Network error"))
        }
    }

    override suspend fun filterHealthConditions(categoryCode: String): Result<List<HealthCondition>> {
        return try {
            val filter = FilterHealthConditionsDto(categoryCode)
            val response = api.filterHealthConditions(filter)

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    val conditions = body.map { dto ->  // ← CAMBIATO (rimosso .value)
                        HealthCondition(
                            healthConditionId = dto.healthConditionId,
                            description = dto.description,
                            category = dto.category?.let { cat ->
                                HealthCategory(
                                    id = cat.id,
                                    code = cat.code,
                                    label = cat.label
                                )
                            }
                        )
                    }
                    Result.Success(conditions)
                } else {
                    Result.Error(AppError.UnknownError("Response body is null"))
                }
            } else {
                Result.Error(AppError.ServerError(response.code(), "Failed to filter conditions"))
            }
        } catch (e: Exception) {
            Result.Error(AppError.NetworkError(e.message ?: "Network error"))
        }
    }

    override suspend fun getUserHealthConditions(userId: Int): Result<List<HealthCondition>> {
        return try {
            val response = api.getUserHealthConditions(userId)

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    val conditions = body.map { dto ->  // ← CAMBIATO (rimosso .value)
                        HealthCondition(
                            healthConditionId = dto.healthConditionId,
                            description = dto.description,
                            category = dto.category?.let { cat ->
                                HealthCategory(
                                    id = cat.id,
                                    code = cat.code,
                                    label = cat.label
                                )
                            }
                        )
                    }
                    Result.Success(conditions)
                } else {
                    Result.Error(AppError.UnknownError("Response body is null"))
                }
            } else {
                Result.Error(AppError.ServerError(response.code(), "Failed to load user conditions"))
            }
        } catch (e: Exception) {
            Result.Error(AppError.NetworkError(e.message ?: "Network error"))
        }
    }

    override suspend fun updateUserHealthConditions(userId: Int, conditionIds: List<String>): Result<Unit> {
        return try {
            val update = UpdateHealthConditionsDto(conditionIds)
            val response = api.updateUserHealthConditions(userId, update)

            if (response.isSuccessful) {
                Result.Success(Unit)
            } else {
                Result.Error(AppError.ServerError(response.code(), "Failed to update conditions"))
            }
        } catch (e: Exception) {
            Result.Error(AppError.NetworkError(e.message ?: "Network error"))
        }
    }

    override suspend fun removeHealthCondition(userId: Int, conditionId: String): Result<Unit> {
        return try {
            val response = api.removeHealthCondition(userId, conditionId)

            if (response.isSuccessful) {
                Result.Success(Unit)
            } else {
                Result.Error(AppError.ServerError(response.code(), "Failed to remove condition"))
            }
        } catch (e: Exception) {
            Result.Error(AppError.NetworkError(e.message ?: "Network error"))
        }
    }
}