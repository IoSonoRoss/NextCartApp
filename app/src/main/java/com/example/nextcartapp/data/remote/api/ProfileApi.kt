package com.example.nextcartapp.data.remote.api

import com.example.nextcartapp.data.remote.dto.UserDto
import retrofit2.Response
import retrofit2.http.GET

interface ProfileApi {
    @GET("profile/profile")
    suspend fun getProfile(): Response<UserDto>  // ← Usa UserDto invece di UserProfileDto
}