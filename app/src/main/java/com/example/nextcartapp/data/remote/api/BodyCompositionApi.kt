package com.example.nextcartapp.data.remote.api

import com.example.nextcartapp.data.remote.dto.BodyCompositionDto
import com.example.nextcartapp.data.remote.dto.CreateBodyCompositionDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface BodyCompositionApi {
    @GET("body-composition/users/{userId}")
    suspend fun getBodyCompositions(@Path("userId") userId: Int): Response<List<BodyCompositionDto>>  // ← CAMBIATO

    @POST("body-composition/users/{userId}")
    suspend fun createBodyComposition(
        @Path("userId") userId: Int,
        @Body composition: CreateBodyCompositionDto
    ): Response<BodyCompositionDto>

    @DELETE("body-composition/users/{userId}/{date}")
    suspend fun deleteBodyComposition(
        @Path("userId") userId: Int,
        @Path("date") date: String
    ): Response<Unit>
}