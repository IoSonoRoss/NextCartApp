package com.example.nextcartapp.data.repository

import android.util.Log
import com.example.nextcartapp.core.util.AppError
import com.example.nextcartapp.core.util.ProductUnit
import com.example.nextcartapp.core.util.Result
import com.example.nextcartapp.data.remote.api.ProductApi
import com.example.nextcartapp.domain.model.NutritionalValue
import com.example.nextcartapp.domain.model.Product
import com.example.nextcartapp.domain.model.ProductDetails
import com.example.nextcartapp.domain.repository.ProductRepository
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val productApi: ProductApi
) : ProductRepository {

    override suspend fun getAllProducts(): Result<List<Product>> {
        return try {
            val response = productApi.getAllProducts()
            if (response.isSuccessful) {
                val dtos = response.body() ?: emptyList()
                val products = dtos.mapNotNull { dto ->

                    android.util.Log.d("DEBUG_ID", "Prodotto da DB: ${dto.name} ha ID: '${dto.productId}'")
                    // Saltiamo i prodotti che non hanno un nome
                    if (dto.name.isNullOrBlank()) null
                    else Product(
                        productId = dto.productId ?: "", // Risolve Type mismatch garantendo String
                        name = dto.name ?: "Prodotto senza nome", // Risolve Type mismatch
                        unitType = try {
                            ProductUnit.valueOf(dto.unitType ?: "UNIT")
                        } catch(e: Exception) {
                            ProductUnit.UNIT
                        },
                        defaultPackageSize = dto.defaultPackageSize,
                        itName = dto.itName,
                        categoryName = dto.productCategory?.category,
                        imageUrl = dto.imageUrl
                    )
                }
                Result.Success(products)
            } else {
                Result.Error(AppError.ServerError(response.code(), "Errore caricamento prodotti"))
            }
        } catch (e: Exception) {
            Result.Error(AppError.NetworkError(e.message ?: "Errore di rete"))
        }
    }

    override suspend fun getProductById(id: String): Result<ProductDetails> {
        if (id.isBlank()) return Result.Error(AppError.UnknownError("ID Prodotto vuoto"))

        return try {
            val response = productApi.getProductById(id)

            if (response.isSuccessful && response.body() != null) {
                val dto = response.body()!!

                val productDetails = ProductDetails(
                    productId = dto.productId,
                    name = dto.name ?: "",
                    itName = dto.itName,
                    unitType = try {
                        ProductUnit.valueOf(dto.unitType ?: "UNIT")
                    } catch (e: Exception) {
                        ProductUnit.UNIT
                    },
                    defaultPackageSize = dto.defaultPackageSize,
                    imageUrl = dto.imageUrl,
                    categoryName = dto.productCategory?.category,
                    standardPortion = dto.productCategory?.standardPortion?.toFloatOrNull(),

                    // MAPPING LISTE (Punta agli oggetti annidati correttamente)
                    diets = dto.productDiets?.mapNotNull { it.diet?.description } ?: emptyList(),
                    claims = dto.productClaims?.mapNotNull { it.claim?.description } ?: emptyList(),
                    allergens = dto.productAllergens?.mapNotNull { it.allergen?.description } ?: emptyList(),

                    // MAPPING NUTRIZIONALE
                    nutritionalValues = dto.nutritionalInformationValues?.mapNotNull { nvDto ->
                        val nutrient = nvDto.nutritionalInformation
                        if (nutrient?.name != null) {
                            NutritionalValue(
                                name = nutrient.name, // Prende nutrientIT dal DTO
                                value = nvDto.value,
                                unit = nutrient.unit ?: "" // Prende unitOfMeasure
                            )
                        } else null
                    } ?: emptyList()
                )
                Result.Success(productDetails)
            } else {
                Result.Error(AppError.ServerError(response.code(), "Prodotto non trovato"))
            }
        } catch (e: Exception) {
            android.util.Log.e("DEBUG_PRODUCT_DETAIL", "Errore: ${e.message}", e)
            Result.Error(AppError.NetworkError(e.message ?: "Errore di rete"))
        }
    }
}