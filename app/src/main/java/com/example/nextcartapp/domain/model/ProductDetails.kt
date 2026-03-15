package com.example.nextcartapp.domain.model

data class ProductDetails(
    val productId: String,
    val name: String,
    val itName: String?,
    val categoryName: String?,
    val standardPortion: String?,
    val diets: List<String>,
    val claims: List<String>,
    val allergens: List<String>,
    val nutritionalValues: List<NutritionalValue>
)

data class NutritionalValue(
    val name: String,
    val value: String,
    val unit: String?
)