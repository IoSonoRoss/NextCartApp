package com.example.nextcartapp.presentation.ui.products.filters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nextcartapp.domain.model.Diet
import com.example.nextcartapp.domain.model.ProductCategory
import com.example.nextcartapp.domain.usecase.filter.GetAllCategoriesUseCase
import com.example.nextcartapp.domain.usecase.filter.GetAllDietsUseCase
import com.example.nextcartapp.core.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FiltersUiState(
    val categories: List<ProductCategory> = emptyList(),
    val diets: List<Diet> = emptyList(),
    val selectedCategories: Set<String> = emptySet(),
    val selectedDiets: Set<String> = emptySet(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class FiltersViewModel @Inject constructor(
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase,
    private val getAllDietsUseCase: GetAllDietsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(FiltersUiState())
    val uiState: StateFlow<FiltersUiState> = _uiState.asStateFlow()

    fun loadCategories() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = getAllCategoriesUseCase()) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            categories = result.data,
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

    fun loadDiets() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = getAllDietsUseCase()) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            diets = result.data,
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

    fun toggleCategory(category: String) {
        _uiState.update { state ->
            val newSelected = if (category in state.selectedCategories) {
                state.selectedCategories - category
            } else {
                state.selectedCategories + category
            }
            state.copy(selectedCategories = newSelected)
        }
    }

    fun toggleDiet(diet: String) {
        _uiState.update { state ->
            val newSelected = if (diet in state.selectedDiets) {
                state.selectedDiets - diet
            } else {
                state.selectedDiets + diet
            }
            state.copy(selectedDiets = newSelected)
        }
    }

    fun getSelectedFilters(): Pair<Set<String>, Set<String>> {
        return _uiState.value.selectedCategories to _uiState.value.selectedDiets
    }
}