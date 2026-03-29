package com.example.nextcartapp.data.remote.api

import com.example.nextcartapp.data.remote.dto.CreateMealDto
import com.example.nextcartapp.data.remote.dto.MealDto
import retrofit2.Response
import retrofit2.http.*

interface MealApi {
    @GET("meal/user/{userId}")
    suspend fun getUserMeals(@Path("userId") userId: Int): Response<List<MealDto>>

    @POST("meal")
    suspend fun createMeal(@Body meal: CreateMealDto): Response<MealDto>
}