package com.example.nextcartapp.data.remote.api

import com.example.nextcartapp.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

interface CartApi {
    @GET("cart/user/{userId}")
    suspend fun getUserCarts(@Path("userId") userId: Int): Response<List<CartDto>>

    @POST("cart")
    suspend fun createCart(@Body cart: CreateCartDto): Response<CartDto>

    @POST("cart/{cartId}/items")
    suspend fun addItemToCart(
        @Path("cartId") cartId: Int,
        @Body item: AddCartItemDto
    ): Response<CartItemDto>

    @DELETE("cart/{cartId}")
    suspend fun deleteCart(@Path("cartId") cartId: Int): Response<Unit>
}