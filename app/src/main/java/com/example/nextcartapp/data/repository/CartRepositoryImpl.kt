package com.example.nextcartapp.data.repository

import com.example.nextcartapp.core.util.*
import com.example.nextcartapp.data.remote.api.CartApi
import com.example.nextcartapp.data.remote.dto.*
import com.example.nextcartapp.domain.model.*
import com.example.nextcartapp.domain.repository.CartRepository
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val api: CartApi
) : CartRepository {

    override suspend fun getUserCarts(userId: Int): Result<List<Cart>> = try {
        val response = api.getUserCarts(userId)
        if (response.isSuccessful && response.body() != null) {
            val carts = response.body()!!.map { dto ->
                Cart(
                    cartId = dto.cartId,
                    name = dto.name,
                    consumerId = userId, // Dato che il server non lo passa, mettiamo un valore dummy o 0
                    items = dto.items.map { itemDto ->
                        CartItem(
                            cartItemId = itemDto.cartItemId.toString(), // Convertiamo l'ID numerico in Stringa per il Domain
                            productId = itemDto.product.productId,      // Accesso all'oggetto annidato
                            productName = itemDto.product.productName,  // Accesso all'oggetto annidato
                            quantity = itemDto.quantity
                        )
                    }
                )
            }
            Result.Success(carts)
        } else {
            Result.Error(AppError.ServerError(response.code(), "Failed"))
        }
    } catch (e: Exception) {
        Result.Error(AppError.NetworkError(e.message ?: "Error"))
    }

    override suspend fun createCart(userId: Int, name: String): Result<Cart> = try {
        val response = api.createCart(CreateCartDto(name, userId))
        if (response.isSuccessful && response.body() != null) {
            val dto = response.body()!!
            Result.Success(Cart(dto.cartId, dto.name, userId))
        } else {
            Result.Error(AppError.ServerError(response.code(), "Failed"))
        }
    } catch (e: Exception) {
        Result.Error(AppError.NetworkError(e.message ?: "Error"))
    }

    override suspend fun addProductToCart(cartId: Int, productId: String): Result<Unit> = try {
        val response = api.addItemToCart(cartId, AddCartItemDto(productId))
        if (response.isSuccessful) Result.Success(Unit)
        else Result.Error(AppError.ServerError(response.code(), "Failed"))
    } catch (e: Exception) {
        Result.Error(AppError.NetworkError(e.message ?: "Error"))
    }

    override suspend fun deleteCart(cartId: Int): Result<Unit> = try {
        val response = api.deleteCart(cartId)
        if (response.isSuccessful) Result.Success(Unit)
        else Result.Error(AppError.ServerError(response.code(), "Failed to delete"))
    } catch (e: Exception) {
        Result.Error(AppError.NetworkError(e.message ?: "Error"))
    }
}