package com.example.nextcartapp.presentation.ui.pantry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nextcartapp.core.util.Result
import com.example.nextcartapp.domain.model.PantryItem
import com.example.nextcartapp.domain.usecase.pantry.ConsumePantryItemUseCase
import com.example.nextcartapp.domain.usecase.pantry.GetPantryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PantryUiState(
    val items: List<PantryItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class PantryViewModel @Inject constructor(
    private val getPantryUseCase: GetPantryUseCase,
    private val consumePantryItemUseCase: ConsumePantryItemUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(PantryUiState())
    val uiState = _uiState.asStateFlow()

    fun loadPantry(userId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            when (val result = getPantryUseCase(userId)) {
                is Result.Success -> {
                    _uiState.update { it.copy(items = result.data, isLoading = false) }
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(isLoading = false, error = "Impossibile caricare la dispensa")
                    }
                }
            }
        }
    }

    fun consumeProduct(userId: Int, pantryItemId: Int, amount: Float) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            // Usa il UseCase, non il repository direttamente
            when (val result = consumePantryItemUseCase(pantryItemId, amount)) {
                is Result.Success<Unit> -> {
                    loadPantry(userId)
                }
                is Result.Error -> {
                    _uiState.update { it.copy(isLoading = false, error = "Errore") }
                }
            }
        }
    }
}