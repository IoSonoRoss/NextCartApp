package com.example.nextcartapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class LoginRequestDto(
    val email: String,
    val password: String
)

data class LoginResponseDto(
    val accessToken: String,
    val user: UserDto? = null  // aggiungiamo i dati utente
)

data class UserDto(
    @SerializedName("consumerId")
    val consumerId: Int,

    @SerializedName("email")
    val email: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("surname")
    val surname: String?,

    @SerializedName("dateOfBirth")
    val dateOfBirth: String?,

    @SerializedName("gender")
    val gender: String?,

    @SerializedName("placeOfBirth")
    val placeOfBirth: String?,

    @SerializedName("address")
    val address: String?,

    @SerializedName("role")
    val role: String?
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

data class ProfileDto(
    val consumerId: Int,
    val email: String,
    val name: String,
    val surname: String?,
    val dateOfBirth: String,
    val gender: String,
    val address: String?,
    val placeOfBirth: String?
)