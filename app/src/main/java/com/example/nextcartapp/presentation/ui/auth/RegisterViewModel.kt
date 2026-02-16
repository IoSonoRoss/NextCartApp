package com.example.nextcartapp.presentation.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nextcartapp.core.util.Result
import com.example.nextcartapp.domain.usecase.auth.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<RegisterUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    // Step 1
    fun onNameChanged(name: String) {
        _uiState.update { it.copy(name = name, nameError = null) }
    }

    fun onSurnameChanged(surname: String) {
        _uiState.update { it.copy(surname = surname) }
    }

    // Step 2
    fun onDateOfBirthChanged(dateOfBirth: String) {
        _uiState.update { it.copy(dateOfBirth = dateOfBirth, dateOfBirthError = null) }
    }

    fun onGenderChanged(gender: String) {
        _uiState.update { it.copy(gender = gender, genderError = null) }
    }

    fun onPlaceOfBirthChanged(placeOfBirth: String) {
        _uiState.update { it.copy(placeOfBirth = placeOfBirth) }
    }

    fun onAddressChanged(address: String) {
        _uiState.update { it.copy(address = address) }
    }

    // Step 3
    fun onEmailChanged(email: String) {
        _uiState.update { it.copy(email = email, emailError = null) }
    }

    // Step 4
    fun onPasswordChanged(password: String) {
        _uiState.update { it.copy(password = password, passwordError = null) }
    }

    fun onConfirmPasswordChanged(confirmPassword: String) {
        _uiState.update { it.copy(confirmPassword = confirmPassword, confirmPasswordError = null) }
    }

    // Validazioni per ogni step
    fun validateStep1(): Boolean {
        var isValid = true
        if (_uiState.value.name.isBlank()) {
            _uiState.update { it.copy(nameError = "Il nome è obbligatorio") }
            isValid = false
        }
        return isValid
    }

    fun validateStep2(): Boolean {
        var isValid = true
        if (_uiState.value.dateOfBirth.isBlank()) {
            _uiState.update { it.copy(dateOfBirthError = "La data di nascita è obbligatoria") }
            isValid = false
        }
        if (_uiState.value.gender.isBlank()) {
            _uiState.update { it.copy(genderError = "Il genere è obbligatorio") }
            isValid = false
        }
        return isValid
    }

    fun validateStep3(): Boolean {
        var isValid = true
        val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")
        if (_uiState.value.email.isBlank()) {
            _uiState.update { it.copy(emailError = "L'email è obbligatoria") }
            isValid = false
        } else if (!emailRegex.matches(_uiState.value.email)) {
            _uiState.update { it.copy(emailError = "Email non valida") }
            isValid = false
        }
        return isValid
    }

    fun validateStep4(): Boolean {
        var isValid = true
        val passwordRegex = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}$")
        if (!passwordRegex.matches(_uiState.value.password)) {
            _uiState.update {
                it.copy(passwordError = "La password non soddisfa i requisiti")
            }
            isValid = false
        }
        if (_uiState.value.password != _uiState.value.confirmPassword) {
            _uiState.update { it.copy(confirmPasswordError = "Le password non coincidono") }
            isValid = false
        }
        return isValid
    }

    fun register() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, generalError = null) }
            val state = _uiState.value
            when (val result = registerUseCase(
                email = state.email,
                password = state.password,
                name = state.name,
                surname = state.surname.ifBlank { null },
                dateOfBirth = state.dateOfBirth,
                gender = state.gender,
                address = state.address.ifBlank { null }
            )) {
                is Result.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _uiEvent.emit(RegisterUiEvent.NavigateToLogin)
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            generalError = result.exception.asUiText()
                        )
                    }
                    _uiEvent.emit(RegisterUiEvent.ShowError(result.exception.asUiText()))
                }
            }
        }
    }

    data class RegisterUiState(
        val name: String = "",
        val surname: String = "",
        val dateOfBirth: String = "",
        val gender: String = "",
        val placeOfBirth: String = "",
        val address: String = "",
        val email: String = "",
        val password: String = "",
        val confirmPassword: String = "",
        val isLoading: Boolean = false,
        val nameError: String? = null,
        val dateOfBirthError: String? = null,
        val genderError: String? = null,
        val emailError: String? = null,
        val passwordError: String? = null,
        val confirmPasswordError: String? = null,
        val generalError: String? = null
    )

    sealed class RegisterUiEvent {
        object NavigateToLogin : RegisterUiEvent()
        data class ShowError(val message: String) : RegisterUiEvent()
    }
}