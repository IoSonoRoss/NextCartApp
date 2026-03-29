package com.example.nextcartapp.domain.repository

import com.example.nextcartapp.core.util.Result
import com.example.nextcartapp.domain.model.Cart

interface CartRepository {
    suspend fun getUserCarts(userId: Int): Result<List<Cart>>
    suspend fun createCart(userId: Int, name: String): Result<Cart>
    suspend fun addProductToCart(cartId: Int, productId: String): Result<Unit>
    suspend fun deleteCart(cartId: Int): Result<Unit>
}