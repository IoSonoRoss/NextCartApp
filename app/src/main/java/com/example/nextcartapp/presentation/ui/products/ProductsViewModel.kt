package com.example.nextcartapp.presentation.ui.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nextcartapp.core.util.Result
import com.example.nextcartapp.domain.model.Product
import com.example.nextcartapp.domain.usecase.product.GetAllProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val getAllProductsUseCase: GetAllProductsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductsUiState())
    val uiState = _uiState.asStateFlow()

    private var allProducts: List<Product> = emptyList()
    private var selectedCategory: String? = null

    init {
        loadProducts()
    }

    fun loadProducts() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = getAllProductsUseCase()) {
                is Result.Success -> {
                    allProducts = result.data
                    val categories = result.data
                        .mapNotNull { it.categoryName }
                        .distinct()
                        .take(10) // Prime 10 categorie

                    android.util.Log.d("DEBUG_CATEGORIES", "Numero categorie: ${categories.size}")
                    android.util.Log.d("DEBUG_CATEGORIES", "Categorie: $categories")

                    _uiState.update {
                        it.copy(
                            products = result.data,
                            categories = categories,
                            isLoading = false
                        )
                    }
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = result.exception.asUiText()
                        )
                    }
                }
            }
        }
    }

    fun onCategorySelected(category: String?) {
        selectedCategory = category
        filterProducts()
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        filterProducts()
    }

    fun applyFilters(categories: List<String>, diets: List<String>) {
        _uiState.update {
            it.copy(
                selectedCategoryFilters = categories,
                selectedDietFilters = diets
            )
        }
        filterProducts()
    }

    private fun filterProducts() {
        val filtered = allProducts.filter { product ->
            val matchesCategory = _uiState.value.selectedCategoryFilters.isEmpty() ||
                    (product.categoryName != null && product.categoryName in _uiState.value.selectedCategoryFilters)  // ← FIX

            val matchesSearch = _uiState.value.searchQuery.isEmpty() ||
                    product.name.contains(_uiState.value.searchQuery, ignoreCase = true) ||
                    product.itName?.contains(_uiState.value.searchQuery, ignoreCase = true) == true

            matchesCategory && matchesSearch
        }

        _uiState.update { it.copy(products = filtered) }
    }

    data class ProductsUiState(
        val products: List<Product> = emptyList(),
        val categories: List<String> = emptyList(),
        val availableCategories: List<String> = emptyList(),
        val selectedCategoryFilters: List<String> = emptyList(),  // ← AGGIUNGI
        val selectedDietFilters: List<String> = emptyList(),      // ← AGGIUNGI
        val searchQuery: String = "",
        val isLoading: Boolean = false,
        val error: String? = null
    )
}