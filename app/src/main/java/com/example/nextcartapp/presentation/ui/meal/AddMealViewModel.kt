package com.example.nextcartapp.presentation.ui.meal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nextcartapp.core.util.Result
import com.example.nextcartapp.domain.usecase.meal.SaveMealUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AddMealUiState(
    val name: String? = null,
    val type: String? = null,
    val date: String? = null,
    val kcal: Float? = null,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class AddMealViewModel @Inject constructor(
    private val saveMealUseCase: SaveMealUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddMealUiState())
    val uiState: StateFlow<AddMealUiState> = _uiState.asStateFlow()

    // Funzioni per aggiornare i singoli campi (chiamate dai Bottom Sheets)

    fun updateName(name: String) {
        _uiState.update { it.copy(name = name.ifBlank { null }) }
    }

    fun updateType(type: String) {
        _uiState.update { it.copy(type = type) }
    }

    fun updateDate(date: String) {
        _uiState.update { it.copy(date = date) }
    }

    fun updateKcal(kcal: Float?) {
        _uiState.update { it.copy(kcal = kcal) }
    }

    /**
     * Esegue il salvataggio definitivo chiamando il Use Case
     */
    fun saveMeal(userId: Int) {
        val currentState = _uiState.value

        // Verifica di sicurezza prima dell'invio
        if (currentState.name == null || currentState.type == null) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val result = saveMealUseCase(
                userId = userId,
                name = currentState.name,
                type = currentState.type,
                kcal = currentState.kcal
            )

            when (result) {
                is Result.Success -> {
                    _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(isLoading = false, error = "Impossibile salvare il pasto")
                    }
                }
            }
        }
    }

    fun resetError() {
        _uiState.update { it.copy(error = null) }
    }
}