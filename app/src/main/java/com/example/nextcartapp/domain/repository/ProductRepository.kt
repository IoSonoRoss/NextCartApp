package com.example.nextcartapp.domain.repository

import com.example.nextcartapp.core.util.Result
import com.example.nextcartapp.domain.model.Product
import com.example.nextcartapp.domain.model.ProductDetails

interface ProductRepository {
    suspend fun getAllProducts(): Result<List<Product>>
    suspend fun getProductById(id: String): Result<ProductDetails>
}