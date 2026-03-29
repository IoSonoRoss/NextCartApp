package com.example.nextcartapp.domain.usecase.meal

import com.example.nextcartapp.core.util.Result
import com.example.nextcartapp.domain.model.Meal
import com.example.nextcartapp.domain.repository.MealRepository
import javax.inject.Inject

class SaveMealUseCase @Inject constructor(
    private val mealRepository: MealRepository
) {
    suspend operator fun invoke(
        userId: Int,
        name: String,
        type: String,
        kcal: Float?
    ): Result<Meal> {
        return mealRepository.saveMeal(userId, name, type, kcal)
    }
}