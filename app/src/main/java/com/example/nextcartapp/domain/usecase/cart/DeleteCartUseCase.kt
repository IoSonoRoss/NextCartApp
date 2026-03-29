package com.example.nextcartapp.domain.usecase.cart

import com.example.nextcartapp.domain.repository.CartRepository
import javax.inject.Inject

class DeleteCartUseCase @Inject constructor(private val repository: CartRepository) {
    suspend operator fun invoke(cartId: Int) = repository.deleteCart(cartId)
}