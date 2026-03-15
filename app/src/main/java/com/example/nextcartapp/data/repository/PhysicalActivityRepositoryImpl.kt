package com.example.nextcartapp.data.repository

import com.example.nextcartapp.core.util.AppError
import com.example.nextcartapp.core.util.Result
import com.example.nextcartapp.data.remote.api.PhysicalActivityApi
import com.example.nextcartapp.data.remote.dto.CreatePhysicalActivityDto
import com.example.nextcartapp.domain.model.PhysicalActivity
import com.example.nextcartapp.domain.repository.PhysicalActivityRepository
import javax.inject.Inject

class PhysicalActivityRepositoryImpl @Inject constructor(
    private val api: PhysicalActivityApi
) : PhysicalActivityRepository {

    override suspend fun getActivities(): Result<List<PhysicalActivity>> {
        return try {
            val response = api.getActivities()

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    val activities = body.map { dto ->  // ← CAMBIATO (rimosso .value)
                        PhysicalActivity(
                            physicalActivityId = dto.physicalActivityId,
                            specificActivity = dto.specificActivity,
                            durationMinutes = dto.durationMinutes,
                            date = dto.date,
                            activityType = dto.activity.activityType,
                            activityId = dto.activity.activityId
                        )
                    }
                    Result.Success(activities)
                } else {
                    Result.Error(AppError.UnknownError("Response body is null"))
                }
            } else {
                Result.Error(AppError.ServerError(response.code(), "Failed to load activities"))
            }
        } catch (e: Exception) {
            Result.Error(AppError.NetworkError(e.message ?: "Network error"))
        }
    }

    override suspend fun createActivity(
        specificActivity: String,
        date: String,
        durationMinutes: Int,
        activityId: Int
    ): Result<PhysicalActivity> {
        return try {
            val dto = CreatePhysicalActivityDto(
                specificActivity = specificActivity,
                date = date,
                durationMinutes = durationMinutes,
                activityId = activityId
            )

            val response = api.createActivity(dto)

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    val activity = PhysicalActivity(
                        physicalActivityId = body.physicalActivityId,
                        specificActivity = body.specificActivity,
                        durationMinutes = body.durationMinutes,
                        date = body.date,
                        activityType = body.activity.activityType,
                        activityId = body.activity.activityId
                    )
                    Result.Success(activity)
                } else {
                    Result.Error(AppError.UnknownError("Response body is null"))
                }
            } else {
                Result.Error(AppError.ServerError(response.code(), "Failed to create activity"))
            }
        } catch (e: Exception) {
            Result.Error(AppError.NetworkError(e.message ?: "Network error"))
        }
    }

    override suspend fun deleteActivity(id: Int): Result<Unit> {
        return try {
            val response = api.deleteActivity(id)

            if (response.isSuccessful) {
                Result.Success(Unit)
            } else {
                Result.Error(AppError.ServerError(response.code(), "Failed to delete activity"))
            }
        } catch (e: Exception) {
            Result.Error(AppError.NetworkError(e.message ?: "Network error"))
        }
    }
}