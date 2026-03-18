package com.example.nextcartapp.presentation.ui.health

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nextcartapp.core.util.Result
import com.example.nextcartapp.domain.model.HealthCondition
import com.example.nextcartapp.domain.usecase.health.FilterHealthConditionsUseCase
import com.example.nextcartapp.domain.usecase.health.GetUserHealthConditionsUseCase
import com.example.nextcartapp.domain.usecase.health.RemoveHealthConditionUseCase
import com.example.nextcartapp.domain.usecase.health.UpdateUserHealthConditionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class NewHealthConditionState(
    val selectedAgeCategory: String? = null,
    val selectedPathologies: List<String> = emptyList(),
    val selectedPhysiologicalStates: List<String> = emptyList()
)

data class HealthUiState(
    val userHealthConditions: List<HealthCondition> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class HealthViewModel @Inject constructor(
    private val getUserHealthConditionsUseCase: GetUserHealthConditionsUseCase,
    private val filterHealthConditionsUseCase: FilterHealthConditionsUseCase,
    private val updateUserHealthConditionsUseCase: UpdateUserHealthConditionsUseCase,
    private val removeHealthConditionUseCase: RemoveHealthConditionUseCase
) : ViewModel() {

    private val _newHealthConditionState = MutableStateFlow(NewHealthConditionState())
    val newHealthConditionState: StateFlow<NewHealthConditionState> = _newHealthConditionState.asStateFlow()

    private val _uiState = MutableStateFlow(HealthUiState())
    val uiState: StateFlow<HealthUiState> = _uiState.asStateFlow()

    fun loadUserHealthConditions(userId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            when (val result = getUserHealthConditionsUseCase(userId)) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            userHealthConditions = result.data,
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

    fun setAgeCategory(ageCategory: String) {
        _newHealthConditionState.update { it.copy(selectedAgeCategory = ageCategory) }
    }

    fun setPathologies(pathologies: List<String>) {
        _newHealthConditionState.update { it.copy(selectedPathologies = pathologies) }
    }

    fun setPhysiologicalStates(states: List<String>) {
        _newHealthConditionState.update { it.copy(selectedPhysiologicalStates = states) }
    }

    fun saveHealthConditions(userId: Int) {
        viewModelScope.launch {
            val state = _newHealthConditionState.value

            // Combina tutte le condizioni selezionate
            val allConditions = mutableListOf<String>()
            state.selectedAgeCategory?.let { allConditions.add(it) }
            allConditions.addAll(state.selectedPathologies)
            allConditions.addAll(state.selectedPhysiologicalStates)

            if (allConditions.isEmpty()) {
                return@launch
            }

            when (updateUserHealthConditionsUseCase(userId, allConditions)) {
                is Result.Success -> {
                    resetNewHealthCondition()
                    loadUserHealthConditions(userId) // Ricarica
                }
                is Result.Error -> {
                    // Gestisci errore se necessario
                }
            }
        }
    }

    fun removeHealthCondition(userId: Int, conditionId: String) {
        viewModelScope.launch {
            when (removeHealthConditionUseCase(userId, conditionId)) {
                is Result.Success -> {
                    loadUserHealthConditions(userId) // Ricarica
                }
                is Result.Error -> {
                    // Gestisci errore
                }
            }
        }
    }

    fun resetNewHealthCondition() {
        _newHealthConditionState.value = NewHealthConditionState()
    }
}