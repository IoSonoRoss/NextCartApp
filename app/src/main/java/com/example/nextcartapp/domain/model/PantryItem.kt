package com.example.nextcartapp.domain.model

data class PantryItem(
    val pantryItemId: Int,
    val product: Product,
    val quantity: Float,
    val lastUpdated: String
)