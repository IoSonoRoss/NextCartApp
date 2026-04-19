package com.example.nextcartapp.domain.model

enum class CartStatus { ACTIVE, COMPLETED }

data class Cart(
    val cartId: Int,
    val name: String,
    val consumerId: Int,
    val status: CartStatus = CartStatus.ACTIVE,
    val items: List<CartItem> = emptyList()
)

data class CartItem(
    val cartItemId: String,
    val productId: String,
    val productName: String,
    val quantity: Int
)