package com.example.nextcartapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class PantryItemDto(
    @SerializedName("pantryItemId") val pantryItemId: Int,
    @SerializedName("quantity") val quantity: Float,
    @SerializedName("last_updated") val lastUpdated: String,
    @SerializedName("product") val product: ProductDto // Usa il tuo ProductDto esistente
)