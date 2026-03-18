package com.example.nextcartapp.domain.usecase.health

import com.example.nextcartapp.core.util.Result
import com.example.nextcartapp.domain.model.HealthCategory
import com.example.nextcartapp.domain.repository.HealthConditionRepository
import javax.inject.Inject

class GetHealthCategoriesUseCase @Inject constructor(
    private val repository: HealthConditionRepository
) {
    suspend operator fun invoke(): Result<List<HealthCategory>> {
        return repository.getCategories()
    }
}