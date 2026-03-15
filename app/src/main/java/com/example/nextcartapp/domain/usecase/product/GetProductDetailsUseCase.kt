package com.example.nextcartapp.domain.usecase.product

import com.example.nextcartapp.domain.model.ProductDetails
import com.example.nextcartapp.domain.repository.ProductRepository
import com.example.nextcartapp.core.util.Result
import javax.inject.Inject

class GetProductDetailsUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(productId: String): Result<ProductDetails> {
        return productRepository.getProductById(productId)
    }
}