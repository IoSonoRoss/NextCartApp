package com.example.nextcartapp.domain.model

data class PhysicalActivity(
    val physicalActivityId: Int,
    val specificActivity: String,
    val durationMinutes: Int,
    val date: String,
    val activityType: String,
    val activityId: Int
)