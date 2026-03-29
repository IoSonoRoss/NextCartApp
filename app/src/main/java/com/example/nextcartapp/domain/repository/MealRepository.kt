package com.example.nextcartapp.domain.repository

import com.example.nextcartapp.core.util.Result
import com.example.nextcartapp.domain.model.Meal

interface MealRepository {
    suspend fun getUserMeals(userId: Int): Result<List<Meal>>
    suspend fun saveMeal(userId: Int, name: String, type: String, kcal: Float?): Result<Meal>
}