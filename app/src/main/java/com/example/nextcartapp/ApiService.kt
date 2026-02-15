package com.example.nextcartapp

import retrofit2.http.*

interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @GET("products/{id}")
    suspend fun getProduct(
        @Path("id") id: String,
        @Header("Authorization") token: String
    ): ProductResponse
}

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val accessToken: String  // 👈 Questo è il campo che il backend restituisce
)

data class ProductResponse(
    val id: String,
    val name: String
)