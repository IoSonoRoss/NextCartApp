package com.example.nextcartapp.domain.usecase.cart

import com.example.nextcartapp.core.util.Result
import com.example.nextcartapp.domain.repository.CartRepository
import javax.inject.Inject

class CheckoutUseCase @Inject constructor(
    private val repository: CartRepository
) {
    suspend operator fun invoke(cartId: Int): Result<Unit> {
        return repository.checkout(cartId)
    }
}