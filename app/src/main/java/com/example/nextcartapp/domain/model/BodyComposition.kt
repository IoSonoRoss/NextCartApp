package com.example.nextcartapp.domain.model

data class BodyComposition(
    val consumerId: Int,
    val measuredAt: String, // ISO 8601 timestamp
    val weight: String,
    val height: String
)