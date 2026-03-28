package com.example.nextcartapp.data.repository

import com.example.nextcartapp.core.util.AppError
import com.example.nextcartapp.core.util.Result
import com.example.nextcartapp.data.remote.api.BodyCompositionApi
import com.example.nextcartapp.data.remote.dto.CreateBodyCompositionDto
import com.example.nextcartapp.domain.model.BodyComposition
import com.example.nextcartapp.domain.repository.BodyCompositionRepository
import javax.inject.Inject

class BodyCompositionRepositoryImpl @Inject constructor(
    private val api: BodyCompositionApi
) : BodyCompositionRepository {

    override suspend fun getBodyCompositions(userId: Int): Result<List<BodyComposition>> {
        return try {
            val response = api.getBodyCompositions(userId)

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    val compositions = body.map { dto ->  // ← CAMBIATO (rimosso .value)
                        BodyComposition(
                            consumerId = dto.consumerId,
                            measuredAt = dto.measuredAt,
                            weight = dto.weight,
                            height = dto.height
                        )
                    }
                    Result.Success(compositions)
                } else {
                    Result.Error(AppError.UnknownError("Response body is null"))
                }
            } else {
                Result.Error(AppError.ServerError(response.code(), "Failed to load body compositions"))
            }
        } catch (e: Exception) {
            Result.Error(AppError.NetworkError(e.message ?: "Network error"))
        }
    }

    override suspend fun createBodyComposition(
        userId: Int,
        date: String,
        weight: Double,
        height: Double
    ): Result<BodyComposition> {
        return try {
            val dto = CreateBodyCompositionDto(
                measuredAt = date,  // ← CAMBIATO da "date" a "measuredAt"
                weight = weight,
                height = height
            )

            val response = api.createBodyComposition(userId, dto)

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    val composition = BodyComposition(
                        consumerId = body.consumerId,
                        measuredAt = body.measuredAt,
                        weight = body.weight,
                        height = body.height
                    )
                    Result.Success(composition)
                } else {
                    Result.Error(AppError.UnknownError("Response body is null"))
                }
            } else {
                Result.Error(AppError.ServerError(response.code(), "Failed to create body composition"))
            }
        } catch (e: Exception) {
            Result.Error(AppError.NetworkError(e.message ?: "Network error"))
        }
    }

    override suspend fun deleteBodyComposition(userId: Int, date: String): Result<Unit> {
        return try {
            val response = api.deleteBodyComposition(userId, date)

            if (response.isSuccessful) {
                Result.Success(Unit)
            } else {
                Result.Error(AppError.ServerError(response.code(), "Failed to delete body composition"))
            }
        } catch (e: Exception) {
            Result.Error(AppError.NetworkError(e.message ?: "Network error"))
        }
    }
}