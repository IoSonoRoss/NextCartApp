package com.example.nextcartapp.domain.repository

import com.example.nextcartapp.domain.model.Diet
import com.example.nextcartapp.domain.model.ProductCategory
import com.example.nextcartapp.core.util.Result

interface FilterRepository {
    suspend fun getAllCategories(): Result<List<ProductCategory>>
    suspend fun getAllDiets(): Result<List<Diet>>
}