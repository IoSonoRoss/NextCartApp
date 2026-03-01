package com.example.nextcartapp.core.network

import com.example.nextcartapp.core.session.SessionManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val sessionManager: SessionManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking {
            sessionManager.accessToken.first()
        }

        val request = if (!token.isNullOrEmpty()) {
            chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            chain.request()
        }

        val response = chain.proceed(request)

        // Se 401, cancella la sessione (token scaduto)
        if (response.code == 401) {
            runBlocking {
                sessionManager.clearSession()
            }
        }

        return response
    }
}