package com.example.nextcartapp.domain.usecase.health

import com.example.nextcartapp.core.util.Result
import com.example.nextcartapp.domain.repository.HealthConditionRepository
import javax.inject.Inject

class RemoveHealthConditionUseCase @Inject constructor(
    private val repository: HealthConditionRepository
) {
    suspend operator fun invoke(userId: Int, conditionId: String): Result<Unit> {
        return repository.removeHealthCondition(userId, conditionId)
    }
}