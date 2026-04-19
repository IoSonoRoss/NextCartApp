package com.example.nextcartapp.presentation.ui.pantry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nextcartapp.core.util.Result
import com.example.nextcartapp.domain.model.PantryItem
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
    private val getPantryUseCase: GetPantryUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PantryUiState())
    val uiState = _uiState.asStateFlow()

    fun loadPantry(userId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = getPantryUseCase(userId)) {
                is Result.Success -> {
                    _uiState.update { it.copy(items = result.data, isLoading = false) }
                }
                is Result.Error -> {
                    _uiState.update { it.copy(isLoading = false, error = "Errore nel caricamento") }
                }
            }
        }
    }
}