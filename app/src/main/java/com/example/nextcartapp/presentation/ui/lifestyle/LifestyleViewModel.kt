package com.example.nextcartapp.presentation.ui.lifestyle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nextcartapp.core.util.Result
import com.example.nextcartapp.domain.model.PhysicalActivity
import com.example.nextcartapp.domain.usecase.lifestyle.CreatePhysicalActivityUseCase
import com.example.nextcartapp.domain.usecase.lifestyle.DeletePhysicalActivityUseCase
import com.example.nextcartapp.domain.usecase.lifestyle.GetPhysicalActivitiesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class NewActivityState(
    val activityType: String? = null,
    val specificActivity: String? = null,
    val date: String? = null,
    val durationMinutes: Int? = null,
    val activityId: Int? = null
)

data class LifestyleUiState(
    val activities: List<PhysicalActivity> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class LifestyleViewModel @Inject constructor(
    private val getPhysicalActivitiesUseCase: GetPhysicalActivitiesUseCase,
    private val createPhysicalActivityUseCase: CreatePhysicalActivityUseCase,
    private val deletePhysicalActivityUseCase: DeletePhysicalActivityUseCase
) : ViewModel() {

    private val _newActivityState = MutableStateFlow(NewActivityState())
    val newActivityState: StateFlow<NewActivityState> = _newActivityState.asStateFlow()

    private val _uiState = MutableStateFlow(LifestyleUiState())
    val uiState: StateFlow<LifestyleUiState> = _uiState.asStateFlow()

    init {
        loadActivities()
    }

    fun loadActivities() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            when (val result = getPhysicalActivitiesUseCase()) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            activities = result.data,
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

    fun setActivityType(activityType: String) {
        android.util.Log.d("LIFESTYLE_VM", "Setting activity type: $activityType")
        _newActivityState.update { it.copy(activityType = activityType) }
    }

    fun setSpecificActivity(specificActivity: String, activityId: Int) {
        android.util.Log.d("LIFESTYLE_VM", "Setting specific activity: $specificActivity, id: $activityId")
        _newActivityState.update {
            it.copy(
                specificActivity = specificActivity,
                activityId = activityId
            )
        }
    }

    fun setDateTime(date: String, durationMinutes: Int) {
        _newActivityState.update {
            it.copy(
                date = date,
                durationMinutes = durationMinutes
            )
        }
    }

    fun resetNewActivity() {
        _newActivityState.value = NewActivityState()
    }

    fun saveActivity() {
        viewModelScope.launch {
            val state = _newActivityState.value

            if (state.specificActivity == null ||
                state.date == null ||
                state.durationMinutes == null ||
                state.activityId == null) {
                android.util.Log.e("LIFESTYLE_VM", "Cannot save: incomplete data")
                return@launch
            }

            // Converti data da dd/MM/yyyy a yyyy-MM-dd
            val dateFormatted = convertDateToISO(state.date)

            when (val result = createPhysicalActivityUseCase(
                specificActivity = state.specificActivity,
                date = dateFormatted,
                durationMinutes = state.durationMinutes,
                activityId = state.activityId
            )) {
                is Result.Success -> {
                    android.util.Log.d("LIFESTYLE_VM", "Activity saved successfully")
                    resetNewActivity()
                    loadActivities() // Ricarica la lista
                }
                is Result.Error -> {
                    android.util.Log.e("LIFESTYLE_VM", "Failed to save activity: ${result.exception.asUiText()}")
                    _uiState.update { it.copy(error = result.exception.asUiText()) }
                }
            }
        }
    }

    fun deleteActivity(id: Int) {
        viewModelScope.launch {
            when (deletePhysicalActivityUseCase(id)) {
                is Result.Success -> {
                    loadActivities() // Ricarica la lista
                }
                is Result.Error -> {
                    // Gestisci errore se necessario
                }
            }
        }
    }

    private fun convertDateToISO(date: String): String {
        // Converte da dd/MM/yyyy a yyyy-MM-dd
        return try {
            val parts = date.split("/")
            if (parts.size == 3) {
                "${parts[2]}-${parts[1]}-${parts[0]}"
            } else {
                date
            }
        } catch (e: Exception) {
            date
        }
    }
}