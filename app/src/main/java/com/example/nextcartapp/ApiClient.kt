package com.example.nextcartapp

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiClient {

    /*
    @POST("api/auth/login") // Cambia il percorso con quello reale del tuo sito
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @GET("api/products/{id}") // Cambia il percorso per i prodotti
    suspend fun getProduct(
        @Header("Authorization") token: String,
        @Path("id") productId: String
    ): ProductResponse
}

object RetrofitClient {
    private const val BASE_URL = "http://192.168.1.95:4300/" // USA IL TUO IP E PORTA

    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}

*/

}