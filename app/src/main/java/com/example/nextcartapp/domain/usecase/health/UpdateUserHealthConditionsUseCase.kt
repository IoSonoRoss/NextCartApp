package com.example.nextcartapp.domain.usecase.health

import com.example.nextcartapp.core.util.Result
import com.example.nextcartapp.domain.repository.HealthConditionRepository
import javax.inject.Inject

class UpdateUserHealthConditionsUseCase @Inject constructor(
    private val repository: HealthConditionRepository
) {
    suspend operator fun invoke(userId: Int, conditionIds: List<String>): Result<Unit> {
        return repository.updateUserHealthConditions(userId, conditionIds)
    }
}