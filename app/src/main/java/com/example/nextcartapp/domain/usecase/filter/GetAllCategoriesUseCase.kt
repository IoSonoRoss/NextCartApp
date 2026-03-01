package com.example.nextcartapp.domain.usecase.filter

import com.example.nextcartapp.domain.model.ProductCategory
import com.example.nextcartapp.domain.repository.FilterRepository
import com.example.nextcartapp.core.util.Result
import javax.inject.Inject

class GetAllCategoriesUseCase @Inject constructor(
    private val filterRepository: FilterRepository
) {
    suspend operator fun invoke(): Result<List<ProductCategory>> {
        return filterRepository.getAllCategories()
    }
}