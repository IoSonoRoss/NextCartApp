package com.example.nextcartapp.data.repository

import com.example.nextcartapp.core.util.AppError
import com.example.nextcartapp.core.util.Result
import com.example.nextcartapp.data.remote.api.ProductApi
import com.example.nextcartapp.domain.model.Product
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

                // LOG: guarda il primo prodotto per vedere la struttura
                if (dtos.isNotEmpty()) {
                    android.util.Log.d("DEBUG_DTO", "=== PRIMO PRODOTTO ===")
                    android.util.Log.d("DEBUG_DTO", "ProductId: ${dtos[0].productId}")
                    android.util.Log.d("DEBUG_DTO", "Name: ${dtos[0].name}")
                    android.util.Log.d("DEBUG_DTO", "ItName: ${dtos[0].itName}")
                    android.util.Log.d("DEBUG_DTO", "ProductCategory oggetto: ${dtos[0].productCategory}")
                    android.util.Log.d("DEBUG_DTO", "CategoryName: ${dtos[0].productCategory?.category}")
                }

                val products = dtos.mapNotNull { dto ->  // ← mapNotNull invece di map
                    if (dto.name.isNullOrBlank()) {
                        android.util.Log.w("DEBUG_PRODUCTS", "Prodotto ${dto.productId} senza nome, saltato")
                        null
                    } else {
                        Product(
                            productId = dto.productId,
                            name = dto.name,  // ← Ora è garantito non-null
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

    override suspend fun getProductById(id: String): Result<Product> {
        return try {
            val response = productApi.getProductById(id)
            if (response.isSuccessful) {
                val dto = response.body()
                if (dto != null) {
                    if (dto.name.isNullOrBlank()) {
                        Result.Error(AppError.UnknownError("Prodotto senza nome"))
                    } else {
                        val product = Product(
                            productId = dto.productId,
                            name = dto.name,  // ← Garantito non-null
                            itName = dto.itName,
                            categoryName = dto.productCategory?.category,
                            imageUrl = null
                        )
                        Result.Success(product)
                    }
                } else {
                    Result.Error(AppError.UnknownError("Prodotto non trovato"))
                }
            } else {
                Result.Error(AppError.ServerError(response.code(), "Errore"))
            }
        } catch (e: Exception) {
            Result.Error(AppError.NetworkError(e.message ?: "Errore di rete"))
        }
    }
}