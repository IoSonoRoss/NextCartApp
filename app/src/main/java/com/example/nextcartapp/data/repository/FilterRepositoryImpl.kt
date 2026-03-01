package com.example.nextcartapp.data.repository

import com.example.nextcartapp.core.util.AppError
import com.example.nextcartapp.data.remote.api.FilterApi
import com.example.nextcartapp.domain.model.Diet
import com.example.nextcartapp.domain.model.ProductCategory
import com.example.nextcartapp.domain.repository.FilterRepository
import com.example.nextcartapp.core.util.Result
import javax.inject.Inject

class FilterRepositoryImpl @Inject constructor(
    private val filterApi: FilterApi
) : FilterRepository {

    override suspend fun getAllCategories(): Result<List<ProductCategory>> {
        return try {
            val response = filterApi.getAllCategories()
            if (response.isSuccessful) {
                val categories = response.body()?.map { dto ->
                    ProductCategory(
                        productCategoryId = dto.productCategoryId,
                        category = dto.category,
                        group = dto.group
                    )
                } ?: emptyList()
                Result.Success(categories)
            } else {
                Result.Error(AppError.ServerError(response.code(), "Errore caricamento categorie"))
            }
        } catch (e: Exception) {
            Result.Error(AppError.NetworkError(e.message ?: "Errore di rete"))
        }
    }

    override suspend fun getAllDiets(): Result<List<Diet>> {
        return try {
            val response = filterApi.getAllDiets()
            if (response.isSuccessful) {
                val diets = response.body()?.map { dto ->
                    Diet(
                        dietId = dto.dietId,
                        description = dto.description,
                        restrictionLevel = dto.restrictionLevel
                    )
                } ?: emptyList()
                Result.Success(diets)
            } else {
                Result.Error(AppError.ServerError(response.code(), "Errore caricamento diete"))
            }
        } catch (e: Exception) {
            Result.Error(AppError.NetworkError(e.message ?: "Errore di rete"))
        }
    }
}