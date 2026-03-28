package com.example.nextcartapp.domain.usecase.bodycomposition

import com.example.nextcartapp.core.util.Result
import com.example.nextcartapp.domain.model.BodyComposition
import com.example.nextcartapp.domain.repository.BodyCompositionRepository
import javax.inject.Inject

class GetBodyCompositionsUseCase @Inject constructor(
    private val repository: BodyCompositionRepository
) {
    suspend operator fun invoke(userId: Int): Result<List<BodyComposition>> {
        return repository.getBodyCompositions(userId)
    }
}