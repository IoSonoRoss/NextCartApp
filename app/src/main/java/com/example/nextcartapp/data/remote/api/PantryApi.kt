package com.example.nextcartapp.data.remote.api

import com.example.nextcartapp.data.remote.dto.PantryItemDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface PantryApi {
    @GET("pantry/user/{userId}")
    suspend fun getUserPantry(@Path("userId") userId: Int): Response<List<PantryItemDto>>
}