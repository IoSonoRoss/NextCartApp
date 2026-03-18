package com.example.nextcartapp.domain.usecase.health

import com.example.nextcartapp.core.util.Result
import com.example.nextcartapp.domain.model.HealthCondition
import com.example.nextcartapp.domain.repository.HealthConditionRepository
import javax.inject.Inject

class GetUserHealthConditionsUseCase @Inject constructor(
    private val repository: HealthConditionRepository
) {
    suspend operator fun invoke(userId: Int): Result<List<HealthCondition>> {
        return repository.getUserHealthConditions(userId)
    }
}