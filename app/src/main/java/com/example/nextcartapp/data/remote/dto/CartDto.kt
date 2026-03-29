package com.example.nextcartapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CartDto(
    @SerializedName("cartId") val cartId: Int,
    @SerializedName("name") val name: String,
    //@SerializedName("consumerId") val consumerId: Int,
    @SerializedName("items") val items: List<CartItemDto> = emptyList()
)

data class CartItemDto(
    @SerializedName("cartItemId") val cartItemId: Int,
    @SerializedName("product") val product: ProductInCartDto,
    @SerializedName("quantity") val quantity: Int
)

data class ProductInCartDto(
    @SerializedName("productId") val productId: String,
    @SerializedName("name") val productName: String
)

data class CreateCartDto(
    @SerializedName("name") val name: String,
    @SerializedName("consumerId") val consumerId: Int
)

data class AddCartItemDto(
    @SerializedName("productId") val productId: String,
    @SerializedName("quantity") val quantity: Int = 1
)