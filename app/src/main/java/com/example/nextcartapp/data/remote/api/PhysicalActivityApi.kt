package com.example.nextcartapp.data.remote.api

import com.example.nextcartapp.data.remote.dto.CreatePhysicalActivityDto
import com.example.nextcartapp.data.remote.dto.PhysicalActivityDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PhysicalActivityApi {
    @GET("physical-activities")
    suspend fun getActivities(): Response<List<PhysicalActivityDto>>  // ← CAMBIATO

    @POST("physical-activities")
    suspend fun createActivity(@Body activity: CreatePhysicalActivityDto): Response<PhysicalActivityDto>

    @DELETE("physical-activities/{id}")
    suspend fun deleteActivity(@Path("id") id: Int): Response<Unit>
}