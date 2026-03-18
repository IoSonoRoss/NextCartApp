package com.example.nextcartapp.domain.repository

import com.example.nextcartapp.core.util.Result
import com.example.nextcartapp.domain.model.HealthCategory
import com.example.nextcartapp.domain.model.HealthCondition

interface HealthConditionRepository {
    suspend fun getCategories(): Result<List<HealthCategory>>
    suspend fun filterHealthConditions(categoryCode: String): Result<List<HealthCondition>>
    suspend fun getUserHealthConditions(userId: Int): Result<List<HealthCondition>>
    suspend fun updateUserHealthConditions(userId: Int, conditionIds: List<String>): Result<Unit>
    suspend fun removeHealthCondition(userId: Int, conditionId: String): Result<Unit>
}