package com.example.nextcartapp.domain.usecase.cart

import com.example.nextcartapp.core.util.Result
import com.example.nextcartapp.domain.model.Cart
import com.example.nextcartapp.domain.repository.CartRepository
import javax.inject.Inject

class CreateCartUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(userId: Int, name: String): Result<Cart> {
        return cartRepository.createCart(userId, name)
    }
}