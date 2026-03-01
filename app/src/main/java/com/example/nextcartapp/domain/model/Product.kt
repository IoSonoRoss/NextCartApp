package com.example.nextcartapp.domain.model

data class Product(
    val productId: String,
    val name: String,
    val itName: String?,
    val categoryName: String?,
    val imageUrl: String?
)

data class ProductCategory(
    val productCategoryId: String,
    val category: String,
    val group: String?
)