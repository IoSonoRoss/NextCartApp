package com.example.nextcartapp.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nextcartapp.core.session.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadUserData()
    }

    private fun loadUserData() {
        viewModelScope.launch {
            sessionManager.userName.collect { name ->
                // Se name è null o "", usa "User"
                val displayName = if (name.isNullOrBlank()) "User" else name

                _uiState.update { it.copy(userName = displayName) }

                // Log di controllo: guarda il Logcat per capire cosa arriva davvero
                android.util.Log.d("DEBUG_HOME", "Nome ricevuto dalla sessione: '$name'")
            }
        }
    }

    data class HomeUiState(
        val userName: String = "User"
    )
}