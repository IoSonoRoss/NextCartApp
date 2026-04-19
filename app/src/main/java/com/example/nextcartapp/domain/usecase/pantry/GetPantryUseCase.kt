package com.example.nextcartapp.domain.usecase.pantry

import com.example.nextcartapp.core.util.Result
import com.example.nextcartapp.domain.model.PantryItem
import com.example.nextcartapp.domain.repository.PantryRepository
import javax.inject.Inject

class GetPantryUseCase @Inject constructor(
    private val repository: PantryRepository
) {
    suspend operator fun invoke(userId: Int): Result<List<PantryItem>> {
        return repository.getUserPantry(userId)
    }
}