package com.example.nextcartapp.domain.model

data class Meal(
    val id: Int,
    val name: String,
    val type: String,
    val date: String,
    val kcal: Float?
)