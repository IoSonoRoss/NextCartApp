package com.example.nextcartapp.domain.usecase.pantry

import com.example.nextcartapp.domain.repository.PantryRepository
import javax.inject.Inject

class ConsumePantryItemUseCase @Inject constructor(private val repository: PantryRepository) {
    suspend operator fun invoke(pantryItemId: Int, amount: Float) =
        repository.consumeItem(pantryItemId, amount)
}