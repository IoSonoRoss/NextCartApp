package com.example.nextcartapp.data.remote.api

import com.example.nextcartapp.data.remote.dto.FilterHealthConditionsDto
import com.example.nextcartapp.data.remote.dto.HealthCategoriesResponse
import com.example.nextcartapp.data.remote.dto.HealthConditionDto
import com.example.nextcartapp.data.remote.dto.UpdateHealthConditionsDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface HealthConditionApi {
    @GET("health-conditions/categories")
    suspend fun getCategories(): Response<HealthCategoriesResponse>

    @POST("health-conditions/filter")
    suspend fun filterHealthConditions(@Body filter: FilterHealthConditionsDto): Response<List<HealthConditionDto>>  // ← CAMBIATO

    @GET("consumer-health-conditions/users/{userId}")
    suspend fun getUserHealthConditions(@Path("userId") userId: Int): Response<List<HealthConditionDto>>

    @PATCH("consumer-health-conditions/users/{userId}")
    suspend fun updateUserHealthConditions(
        @Path("userId") userId: Int,
        @Body update: UpdateHealthConditionsDto
    ): Response<Unit>

    @DELETE("consumer-health-conditions/users/{userId}/health-conditions/{conditionId}")
    suspend fun removeHealthCondition(
        @Path("userId") userId: Int,
        @Path("conditionId") conditionId: String
    ): Response<Unit>
}