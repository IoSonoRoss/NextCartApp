package com.example.nextcartapp.domain.repository

import com.example.nextcartapp.domain.model.User
import com.example.nextcartapp.core.util.Result

interface UserRepository {
    suspend fun getProfile(): Result<User>
    suspend fun updateProfile(user: User): Result<User>
}