package com.example.nextcartapp.domain.usecase.filter

import com.example.nextcartapp.domain.model.Diet
import com.example.nextcartapp.domain.repository.FilterRepository
import com.example.nextcartapp.core.util.Result
import javax.inject.Inject

class GetAllDietsUseCase @Inject constructor(
    private val filterRepository: FilterRepository
) {
    suspend operator fun invoke(): Result<List<Diet>> {
        return filterRepository.getAllDiets()
    }
}