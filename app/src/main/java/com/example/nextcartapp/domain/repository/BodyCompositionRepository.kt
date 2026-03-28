package com.example.nextcartapp.domain.repository

import com.example.nextcartapp.core.util.Result
import com.example.nextcartapp.domain.model.BodyComposition

interface BodyCompositionRepository {
    suspend fun getBodyCompositions(userId: Int): Result<List<BodyComposition>>
    suspend fun createBodyComposition(
        userId: Int,
        date: String,
        weight: Double,
        height: Double
    ): Result<BodyComposition>
    suspend fun deleteBodyComposition(userId: Int, date: String): Result<Unit>
}