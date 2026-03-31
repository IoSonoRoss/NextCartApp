package com.example.nextcartapp.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nextcartapp.core.session.SessionManager
import com.example.nextcartapp.core.util.Result
import com.example.nextcartapp.domain.model.Cart
import com.example.nextcartapp.domain.model.Product
import com.example.nextcartapp.domain.usecase.cart.GetCartUseCase
import com.example.nextcartapp.domain.usecase.product.GetAllProductsUseCase // Assumi che esista
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val userName: String = "User",
    val favorites: List<Product> = emptyList(),
    val carts: List<Cart> = emptyList(),
    val suggested: List<Product> = emptyList(),
    val isLoading: Boolean = false
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    private val getCartUseCase: GetCartUseCase,
    // Se non hai ancora GetProductsUseCase, caricheremo solo i carrelli per ora
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadDashboardData()
    }

    private fun loadDashboardData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            // 1. Carica Nome Utente
            launch {
                sessionManager.userName.collect { name ->
                    _uiState.update { it.copy(userName = name ?: "User") }
                }
            }

            // 2. Recupera l'ID (Conversione String -> Int)
            val userIdString = sessionManager.userId.first() ?: "1"
            val userId = userIdString.toIntOrNull() ?: 1

            // 3. Carica i Carrelli
            when (val result = getCartUseCase(userId)) {
                is Result.Success -> {
                    _uiState.update { it.copy(carts = result.data, isLoading = false) }
                }
                is Result.Error -> {
                    _uiState.update { it.copy(isLoading = false) }
                }
            }
        }
    }
}