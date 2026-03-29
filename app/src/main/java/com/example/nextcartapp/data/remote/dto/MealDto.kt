package com.example.nextcartapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class MealDto(
    @SerializedName("mealId") val mealId: Int,
    @SerializedName("name") val name: String,
    @SerializedName("type") val type: String, // BREAKFAST, LUNCH, ecc.
    @SerializedName("timestamp") val timestamp: String,
    @SerializedName("kcal") val kcal: Float?
)

data class CreateMealDto(
    @SerializedName("name") val name: String,
    @SerializedName("type") val type: String,
    @SerializedName("kcal") val kcal: Float?,
    @SerializedName("consumerId") val consumerId: Int
)