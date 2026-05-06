package com.example.nextcartapp.domain.model

import com.example.nextcartapp.core.util.ProductUnit

data class ProductDetails(
    val productId: String,
    val name: String,
    val itName: String?,
    val unitType: ProductUnit,       // <--- Fondamentale per la dispensa
    val defaultPackageSize: Float?,  // <--- Fondamentale per la dispensa
    val imageUrl: String?,
    val categoryName: String?,
    val standardPortion: Float?,     // Mantenuto dal tuo codice
    val diets: List<String> = emptyList(),
    val claims: List<String> = emptyList(),
    val allergens: List<String> = emptyList(),
    val nutritionalValues: List<NutritionalValue> = emptyList()
)

data class NutritionalValue(
    val name: String, // Cambiato da nutrientName per coerenza col tuo repository
    val value: Float?,
    val unit: String
)