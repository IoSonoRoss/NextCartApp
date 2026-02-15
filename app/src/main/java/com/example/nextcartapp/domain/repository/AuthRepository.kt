package com.example.nextcartapp.domain.repository

import com.example.nextcartapp.core.util.Result

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<String>
    suspend fun register(
        email: String,
        password: String,
        name: String,
        surname: String?,
        dateOfBirth: String,
        gender: String,
        address: String?
    ): Result<Int>
    suspend fun logout(): Result<Unit>
}