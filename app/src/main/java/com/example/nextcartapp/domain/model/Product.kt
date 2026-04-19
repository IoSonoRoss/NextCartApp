package com.example.nextcartapp.domain.model

import com.example.nextcartapp.core.util.ProductUnit

data class Product(
    val productId: String,
    val name: String,
    val unitType: ProductUnit,
    val defaultPackageSize: Float? = null,
    val itName: String?,
    val categoryName: String?,
    val imageUrl: String?
)

data class ProductCategory(
    val productCategoryId: String,
    val category: String,
    val group: String?
)