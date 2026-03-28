package com.example.nextcartapp.presentation.ui.bodycomposition

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nextcartapp.core.util.Result
import com.example.nextcartapp.domain.model.BodyComposition
import com.example.nextcartapp.domain.usecase.bodycomposition.CreateBodyCompositionUseCase
import com.example.nextcartapp.domain.usecase.bodycomposition.DeleteBodyCompositionUseCase
import com.example.nextcartapp.domain.usecase.bodycomposition.GetBodyCompositionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class NewBodyCompositionState(
    val dateTime: String? = null,
    val weight: Double? = null,
    val height: Double? = null
)

data class BodyCompositionUiState(
    val compositions: List<BodyComposition> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class BodyCompositionViewModel @Inject constructor(
    private val getBodyCompositionsUseCase: GetBodyCompositionsUseCase,
    private val createBodyCompositionUseCase: CreateBodyCompositionUseCase,
    private val deleteBodyCompositionUseCase: DeleteBodyCompositionUseCase
) : ViewModel() {

    private val _newCompositionState = MutableStateFlow(NewBodyCompositionState())
    val newCompositionState: StateFlow<NewBodyCompositionState> = _newCompositionState.asStateFlow()

    private val _uiState = MutableStateFlow(BodyCompositionUiState())
    val uiState: StateFlow<BodyCompositionUiState> = _uiState.asStateFlow()

    fun loadBodyCompositions(userId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            when (val result = getBodyCompositionsUseCase(userId)) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            compositions = result.data,
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

    fun setDateTime(dateTime: String) {
        _newCompositionState.update { it.copy(dateTime = dateTime) }
    }

    fun setWeight(weight: Double) {
        _newCompositionState.update { it.copy(weight = weight) }
    }

    fun setHeight(height: Double) {
        _newCompositionState.update { it.copy(height = height) }
    }

    fun saveBodyComposition(userId: Int) {
        viewModelScope.launch {
            val state = _newCompositionState.value

            if (state.dateTime == null || state.weight == null || state.height == null) {
                android.util.Log.e("BODY_COMPOSITION_VM", "Cannot save: incomplete data")
                return@launch
            }

            // Il backend si aspetta "yyyy-MM-dd HH:mm:ss" (NON ISO con T)
            android.util.Log.d("BODY_COMPOSITION_VM", "Saving: userId=$userId, dateTime=${state.dateTime}, weight=${state.weight}, height=${state.height}")

            when (val result = createBodyCompositionUseCase(
                userId = userId,
                date = state.dateTime,  // ← Usa formato originale (con spazio, senza T)
                weight = state.weight,
                height = state.height
            )) {
                is Result.Success -> {
                    android.util.Log.d("BODY_COMPOSITION_VM", "Body composition saved successfully")
                    resetNewComposition()
                    loadBodyCompositions(userId)
                }
                is Result.Error -> {
                    android.util.Log.e("BODY_COMPOSITION_VM", "Failed to save: ${result.exception.asUiText()}")
                    _uiState.update { it.copy(error = result.exception.asUiText()) }
                }
            }
        }
    }

    fun deleteBodyComposition(userId: Int, date: String) {
        viewModelScope.launch {
            // Estrai solo la data (yyyy-MM-dd) dal timestamp ISO
            val dateOnly = date.substringBefore("T")

            when (deleteBodyCompositionUseCase(userId, dateOnly)) {
                is Result.Success -> {
                    loadBodyCompositions(userId) // Ricarica
                }
                is Result.Error -> {
                    // Gestisci errore
                }
            }
        }
    }

    fun resetNewComposition() {
        _newCompositionState.value = NewBodyCompositionState()
    }
}