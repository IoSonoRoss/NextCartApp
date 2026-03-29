package com.example.nextcartapp.data.repository

import com.example.nextcartapp.core.util.AppError
import com.example.nextcartapp.core.util.Result
import com.example.nextcartapp.data.remote.api.MealApi
import com.example.nextcartapp.data.remote.dto.CreateMealDto
import com.example.nextcartapp.domain.model.Meal
import com.example.nextcartapp.domain.repository.MealRepository
import javax.inject.Inject

class MealRepositoryImpl @Inject constructor(
    private val api: MealApi
) : MealRepository {

    override suspend fun getUserMeals(userId: Int): Result<List<Meal>> = try {
        val response = api.getUserMeals(userId)
        if (response.isSuccessful && response.body() != null) {
            val meals = response.body()!!.map { dto ->
                Meal(
                    id = dto.mealId,
                    name = dto.name,
                    type = dto.type,
                    date = dto.timestamp,
                    kcal = dto.kcal
                )
            }
            Result.Success(meals)
        } else {
            Result.Error(AppError.ServerError(response.code(), "Errore nel recupero dei pasti"))
        }
    } catch (e: Exception) {
        Result.Error(AppError.NetworkError(e.message ?: "Errore di connessione"))
    }

    override suspend fun saveMeal(userId: Int, name: String, type: String, kcal: Float?): Result<Meal> = try {
        val response = api.createMeal(CreateMealDto(name, type, kcal, userId))
        if (response.isSuccessful && response.body() != null) {
            val dto = response.body()!!
            Result.Success(
                Meal(
                    id = dto.mealId,
                    name = dto.name,
                    type = dto.type,
                    date = dto.timestamp,
                    kcal = dto.kcal
                )
            )
        } else {
            Result.Error(AppError.ServerError(response.code(), "Errore durante il salvataggio"))
        }
    } catch (e: Exception) {
        Result.Error(AppError.NetworkError(e.message ?: "Errore di rete"))
    }
}