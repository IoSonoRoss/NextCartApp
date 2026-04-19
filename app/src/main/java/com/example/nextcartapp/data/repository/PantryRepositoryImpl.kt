package com.example.nextcartapp.data.repository

import com.example.nextcartapp.core.util.AppError
import com.example.nextcartapp.core.util.ProductUnit
import com.example.nextcartapp.core.util.Result
import com.example.nextcartapp.data.remote.api.PantryApi
import com.example.nextcartapp.domain.model.PantryItem
import com.example.nextcartapp.domain.model.Product
import com.example.nextcartapp.domain.repository.PantryRepository
import javax.inject.Inject

class PantryRepositoryImpl @Inject constructor(
    private val api: PantryApi
) : PantryRepository {

    override suspend fun getUserPantry(userId: Int): Result<List<PantryItem>> = try {
        val response = api.getUserPantry(userId)

        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                val pantryItems = body.map { dto ->
                    PantryItem(
                        pantryItemId = dto.pantryItemId, // Assicurati che nel modello si chiami 'id'
                        quantity = dto.quantity,
                        lastUpdated = dto.lastUpdated,
                        product = Product(
                            productId = dto.product.productId,
                            name = dto.product.name ?: "Sconosciuto",
                            unitType = try {
                                ProductUnit.valueOf(dto.product.unitType)
                            } catch (e: Exception) {
                                ProductUnit.UNIT
                            },
                            defaultPackageSize = dto.product.defaultPackageSize,
                            itName = dto.product.itName,
                            categoryName = dto.product.productCategory?.category,
                            imageUrl = null
                        )
                    )
                }
                Result.Success(pantryItems)
            } else {
                Result.Success(emptyList())
            }
        } else {
            Result.Error(AppError.ServerError(response.code(), "Errore API"))
        }
    } catch (e: Exception) {
        Result.Error(AppError.NetworkError(e.message ?: "Errore di rete"))
    }
}