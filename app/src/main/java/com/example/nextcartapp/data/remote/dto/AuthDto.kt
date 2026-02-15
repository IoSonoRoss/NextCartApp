package com.example.nextcartapp.data.remote.dto

data class LoginRequestDto(
    val email: String,
    val password: String
)

data class LoginResponseDto(
    val accessToken: String
)

data class RegisterRequestDto(
    val email: String,
    val password: String,
    val name: String,
    val surname: String?,
    val dateOfBirth: String,
    val gender: String,
    val address: String?
)

data class RegisterResponseDto(
    val message: String,
    val userId: Int
)