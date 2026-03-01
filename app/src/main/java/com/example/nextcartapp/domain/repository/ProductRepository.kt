package com.example.nextcartapp.domain.repository

import com.example.nextcartapp.core.util.Result
import com.example.nextcartapp.domain.model.Product

interface ProductRepository {
    suspend fun getAllProducts(): Result<List<Product>>
    suspend fun getProductById(id: String): Result<Product>
}