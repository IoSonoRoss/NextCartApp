package com.example.nextcartapp.domain.usecase.lifestyle

import com.example.nextcartapp.domain.model.PhysicalActivity
import com.example.nextcartapp.domain.repository.PhysicalActivityRepository
import com.example.nextcartapp.core.util.Result
import javax.inject.Inject

class GetPhysicalActivitiesUseCase @Inject constructor(
    private val repository: PhysicalActivityRepository
) {
    suspend operator fun invoke(): Result<List<PhysicalActivity>> {
        return repository.getActivities()
    }
}