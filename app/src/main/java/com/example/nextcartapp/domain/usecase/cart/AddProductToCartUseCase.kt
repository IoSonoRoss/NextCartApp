package com.example.nextcartapp.domain.usecase.cart

import com.example.nextcartapp.core.util.Result
import com.example.nextcartapp.domain.repository.CartRepository
import javax.inject.Inject

class AddProductToCartUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(cartId: Int, productId: String): Result<Unit> {
        return cartRepository.addProductToCart(cartId, productId)
    }
}