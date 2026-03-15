package com.example.nextcartapp.data.repository

import com.example.nextcartapp.core.util.AppError
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
            android.util.Log.d("DEBUG_PRODUCTS", "Chiamata getAllProducts...")
            val response = productApi.getAllProducts()
            android.util.Log.d("DEBUG_PRODUCTS", "Response code: ${response.code()}")

            if (response.isSuccessful) {
                val dtos = response.body() ?: emptyList()
                android.util.Log.d("DEBUG_PRODUCTS", "Prodotti ricevuti: ${dtos.size}")

                if (dtos.isNotEmpty()) {
                    android.util.Log.d("DEBUG_DTO", "=== PRIMO PRODOTTO ===")
                    android.util.Log.d("DEBUG_DTO", "ProductId: ${dtos[0].productId}")
                    android.util.Log.d("DEBUG_DTO", "Name: ${dtos[0].name}")
                    android.util.Log.d("DEBUG_DTO", "ItName: ${dtos[0].itName}")
                    android.util.Log.d("DEBUG_DTO", "ProductCategory oggetto: ${dtos[0].productCategory}")
                    android.util.Log.d("DEBUG_DTO", "CategoryName: ${dtos[0].productCategory?.category}")
                }

                val products = dtos.mapNotNull { dto ->
                    if (dto.name.isNullOrBlank()) {
                        android.util.Log.w("DEBUG_PRODUCTS", "Prodotto ${dto.productId} senza nome, saltato")
                        null
                    } else {
                        Product(
                            productId = dto.productId,
                            name = dto.name,
                            itName = dto.itName,
                            categoryName = dto.productCategory?.category,
                            imageUrl = null
                        )
                    }
                }

                android.util.Log.d("DEBUG_PRODUCTS", "Prodotti mappati: ${products.size}")
                Result.Success(products)
            } else {
                android.util.Log.e("DEBUG_PRODUCTS", "Errore: ${response.code()}")
                Result.Error(AppError.ServerError(response.code(), "Errore caricamento prodotti"))
            }
        } catch (e: Exception) {
            android.util.Log.e("DEBUG_PRODUCTS", "Eccezione: ${e.message}", e)
            Result.Error(AppError.NetworkError(e.message ?: "Errore di rete"))
        }
    }

    override suspend fun getProductById(id: String): Result<ProductDetails> {
        return try {
            val response = productApi.getProductById(id)

            if (response.isSuccessful) {
                val dto = response.body()

                if (dto != null && !dto.name.isNullOrBlank()) {
                    val productDetails = ProductDetails(
                        productId = dto.productId,
                        name = dto.name,
                        itName = dto.itName,
                        categoryName = dto.productCategory?.category,
                        standardPortion = dto.productCategory?.standardPortion,
                        diets = dto.productDiets?.mapNotNull { it.diet?.description } ?: emptyList(),
                        claims = dto.productClaims?.mapNotNull { it.claim?.description } ?: emptyList(),
                        allergens = dto.productAllergens?.mapNotNull { it.allergen?.description } ?: emptyList(),
                        nutritionalValues = dto.nutritionalInformationValues?.mapNotNull { nvDto ->
                            val nutrient = nvDto.nutritionalInformation
                            if (nutrient?.name != null && nvDto.value != null) {
                                NutritionalValue(
                                    name = nutrient.name,
                                    value = nvDto.value,
                                    unit = nutrient.unit
                                )
                            } else null
                        } ?: emptyList()
                    )

                    Result.Success(productDetails)
                } else {
                    Result.Error(AppError.UnknownError("Prodotto non trovato o senza nome"))
                }
            } else {
                Result.Error(AppError.ServerError(response.code(), "Errore caricamento dettaglio"))
            }
        } catch (e: Exception) {
            android.util.Log.e("DEBUG_PRODUCT_DETAIL", "Errore: ${e.message}", e)
            Result.Error(AppError.NetworkError(e.message ?: "Errore di rete"))
        }
    }
}