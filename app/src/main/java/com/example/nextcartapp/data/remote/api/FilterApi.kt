package com.example.nextcartapp.data.remote.api

import com.example.nextcartapp.data.remote.dto.DietDto
import com.example.nextcartapp.data.remote.dto.ProductCategoryDto
import retrofit2.Response
import retrofit2.http.GET

interface FilterApi {
    @GET("product-categories")
    suspend fun getAllCategories(): Response<List<ProductCategoryDto>>

    @GET("diet")
    suspend fun getAllDiets(): Response<List<DietDto>>
}