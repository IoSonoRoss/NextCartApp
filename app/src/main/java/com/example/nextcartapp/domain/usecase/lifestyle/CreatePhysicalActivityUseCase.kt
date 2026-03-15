package com.example.nextcartapp.domain.usecase.lifestyle

import com.example.nextcartapp.domain.model.PhysicalActivity
import com.example.nextcartapp.domain.repository.PhysicalActivityRepository
import com.example.nextcartapp.core.util.Result
import javax.inject.Inject

class CreatePhysicalActivityUseCase @Inject constructor(
    private val repository: PhysicalActivityRepository
) {
    suspend operator fun invoke(
        specificActivity: String,
        date: String,
        durationMinutes: Int,
        activityId: Int
    ): Result<PhysicalActivity> {
        return repository.createActivity(
            specificActivity = specificActivity,
            date = date,
            durationMinutes = durationMinutes,
            activityId = activityId
        )
    }
}