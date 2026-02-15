package com.example.nextcartapp.data.remote.api

import com.example.nextcartapp.data.remote.dto.LoginRequestDto
import com.example.nextcartapp.data.remote.dto.LoginResponseDto
import com.example.nextcartapp.data.remote.dto.RegisterRequestDto
import com.example.nextcartapp.data.remote.dto.RegisterResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequestDto): Response<LoginResponseDto>

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequestDto): Response<RegisterResponseDto>

    @POST("auth/logout")
    suspend fun logout(): Response<Unit>

    @POST("auth/refresh")
    suspend fun refresh(): Response<LoginResponseDto>
}