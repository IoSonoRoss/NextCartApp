package com.example.nextcartapp.presentation.ui.products.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nextcartapp.domain.model.ProductDetails
import com.example.nextcartapp.domain.usecase.product.GetProductDetailsUseCase
import com.example.nextcartapp.core.util.Result
import com.example.nextcartapp.core.util.AppError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProductDetailsUiState(
    val productDetails: ProductDetails? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val getProductDetailsUseCase: GetProductDetailsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductDetailsUiState())
    val uiState: StateFlow<ProductDetailsUiState> = _uiState.asStateFlow()

    fun loadProductDetails(productId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            when (val result = getProductDetailsUseCase(productId)) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            productDetails = result.data,
                            isLoading = false,
                            error = null
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
}