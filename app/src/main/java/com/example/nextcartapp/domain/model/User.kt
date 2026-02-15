package com.example.nextcartapp.domain.model

data class User(
    val userId: Int,
    val email: String,
    val name: String,
    val surname: String?,
    val gender: String,
    val dateOfBirth: String,
    val address: String?
)