package com.example.nextcartapp.data.remote.api

import com.example.nextcartapp.data.remote.dto.ProductDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductApi {
    @GET("products")
    suspend fun getAllProducts(): Response<List<ProductDto>>

    @GET("products/{id}")
    suspend fun getProductById(@Path("id") id: String): Response<ProductDto>
}