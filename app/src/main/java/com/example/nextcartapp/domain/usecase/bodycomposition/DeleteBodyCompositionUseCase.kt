package com.example.nextcartapp.domain.usecase.bodycomposition

import com.example.nextcartapp.core.util.Result
import com.example.nextcartapp.domain.repository.BodyCompositionRepository
import javax.inject.Inject

class DeleteBodyCompositionUseCase @Inject constructor(
    private val repository: BodyCompositionRepository
) {
    suspend operator fun invoke(userId: Int, date: String): Result<Unit> {
        return repository.deleteBodyComposition(userId, date)
    }
}