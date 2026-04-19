package com.example.nextcartapp.domain.repository

import com.example.nextcartapp.core.util.Result
import com.example.nextcartapp.domain.model.PantryItem

interface PantryRepository {
    suspend fun getUserPantry(userId: Int): Result<List<PantryItem>>
}