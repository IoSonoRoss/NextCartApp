package com.example.nextcartapp.domain.usecase.lifestyle

import com.example.nextcartapp.domain.repository.PhysicalActivityRepository
import com.example.nextcartapp.core.util.Result
import javax.inject.Inject

class DeletePhysicalActivityUseCase @Inject constructor(
    private val repository: PhysicalActivityRepository
) {
    suspend operator fun invoke(id: Int): Result<Unit> {
        return repository.deleteActivity(id)
    }
}