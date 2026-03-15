package com.example.nextcartapp.domain.repository

import com.example.nextcartapp.domain.model.PhysicalActivity
import com.example.nextcartapp.core.util.Result

interface PhysicalActivityRepository {
    suspend fun getActivities(): Result<List<PhysicalActivity>>
    suspend fun createActivity(
        specificActivity: String,
        date: String,
        durationMinutes: Int,
        activityId: Int
    ): Result<PhysicalActivity>
    suspend fun deleteActivity(id: Int): Result<Unit>
}