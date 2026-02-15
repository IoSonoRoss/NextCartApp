package com.example.nextcartapp.core.util

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: AppError) : Result<Nothing>()
}

sealed class AppError {
    data class NetworkError(val message: String) : AppError()
    data class ServerError(val code: Int, val message: String) : AppError()
    data class UnauthorizedError(val message: String = "Sessione scaduta") : AppError()
    data class UnknownError(val message: String) : AppError()

    fun asUiText(): String = when (this) {
        is NetworkError -> "Errore di rete: $message"
        is ServerError -> "Errore server ($code): $message"
        is UnauthorizedError -> message
        is UnknownError -> message
    }
}