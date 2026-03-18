package com.example.nextcartapp.domain.model

data class HealthCondition(
    val healthConditionId: String,
    val description: String,
    val category: HealthCategory? = null
)