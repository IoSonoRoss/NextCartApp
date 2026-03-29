package com.example.nextcartapp.domain.usecase.cart

import com.example.nextcartapp.core.util.Result
import com.example.nextcartapp.domain.model.Cart
import com.example.nextcartapp.domain.repository.CartRepository
import javax.inject.Inject

class GetCartUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(userId: Int): Result<List<Cart>> {
        return cartRepository.getUserCarts(userId)
    }
}