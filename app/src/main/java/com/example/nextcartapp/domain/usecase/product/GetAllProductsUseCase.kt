package com.example.nextcartapp.domain.usecase.product

import com.example.nextcartapp.core.util.Result
import com.example.nextcartapp.domain.model.Product
import com.example.nextcartapp.domain.repository.ProductRepository
import javax.inject.Inject

class GetAllProductsUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(): Result<List<Product>> {
        return productRepository.getAllProducts()
    }
}