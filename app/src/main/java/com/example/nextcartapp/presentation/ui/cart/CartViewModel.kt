package com.example.nextcartapp.presentation.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nextcartapp.core.util.Result
import com.example.nextcartapp.domain.model.Cart
import com.example.nextcartapp.domain.usecase.cart.AddProductToCartUseCase
import com.example.nextcartapp.domain.usecase.cart.CreateCartUseCase
import com.example.nextcartapp.domain.usecase.cart.DeleteCartUseCase
import com.example.nextcartapp.domain.usecase.cart.GetCartUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Rappresenta lo stato della UI per la selezione e creazione del carrello.
 */
data class CartSelectionUiState(
    val carts: List<Cart> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val actionSuccess: Boolean = false
)

@HiltViewModel
class CartViewModel @Inject constructor(
    private val getCartUseCase: GetCartUseCase,
    private val addProductToCartUseCase: AddProductToCartUseCase,
    private val createCartUseCase: CreateCartUseCase,
    private val deleteCartUseCase: DeleteCartUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CartSelectionUiState())
    val uiState: StateFlow<CartSelectionUiState> = _uiState.asStateFlow()

    /**
     * Resetta lo stato di successo e l'errore.
     * Da chiamare quando il BottomSheet viene chiuso o riaperto.
     */
    fun resetActionState() {
        _uiState.update { it.copy(actionSuccess = false, error = null) }
    }

    /**
     * Carica i carrelli associati all'utente specifico.
     * @param userId L'ID dell'utente loggato recuperato dal profilo.
     */
    fun loadUserCarts(userId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            when (val result = getCartUseCase(userId)) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(carts = result.data, isLoading = false)
                    }
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(isLoading = false, error = "Errore durante il caricamento dei carrelli")
                    }
                }
            }
        }
    }

    /**
     * Aggiunge un prodotto a un carrello già esistente.
     */
    fun addProductToSelectedCart(cartId: Int, productId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            when (val result = addProductToCartUseCase(cartId, productId)) {
                is Result.Success -> {
                    _uiState.update { it.copy(isLoading = false, actionSuccess = true) }
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(isLoading = false, error = "Errore nell'aggiunta del prodotto al carrello")
                    }
                }
            }
        }
    }

    /**
     * Esegue una doppia operazione: crea un nuovo carrello e vi aggiunge immediatamente il prodotto.
     * @param userId ID utente che crea il carrello.
     * @param cartName Nome assegnato al nuovo carrello.
     * @param productId ID del prodotto da aggiungere dopo la creazione.
     */
    fun createCartAndAddProduct(userId: Int, cartName: String, productId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            // 1. Fase di creazione del carrello
            when (val createResult = createCartUseCase(userId, cartName)) {
                is Result.Success -> {
                    val newCartId = createResult.data.cartId

                    // 2. Fase di aggiunta del prodotto (automatica dopo il successo della creazione)
                    when (val addResult = addProductToCartUseCase(newCartId, productId)) {
                        is Result.Success -> {
                            _uiState.update { it.copy(isLoading = false, actionSuccess = true) }
                        }
                        is Result.Error -> {
                            _uiState.update {
                                it.copy(isLoading = false, error = "Carrello creato, ma errore nell'inserimento del prodotto")
                            }
                        }
                    }
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(isLoading = false, error = "Impossibile creare il nuovo carrello")
                    }
                }
            }
        }
    }

    fun createCart(userId: Int, name: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = createCartUseCase(userId, name)) {
                is Result.Success -> {
                    // Dopo la creazione, ricarichiamo la lista per mostrare il nuovo carrello
                    loadUserCarts(userId)
                    _uiState.update { it.copy(actionSuccess = true) }
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(isLoading = false, error = "Errore durante la creazione del carrello")
                    }
                }
            }
        }
    }

    /**
     * Elimina un carrello esistente.
     */
    fun deleteCart(userId: Int, cartId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            // Assicurati di aver iniettato deleteCartUseCase nel costruttore del ViewModel
            when (val result = deleteCartUseCase(cartId)) {
                is Result.Success -> {
                    // Dopo l'eliminazione, ricarichiamo la lista aggiornata
                    loadUserCarts(userId)
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(isLoading = false, error = "Impossibile eliminare il carrello")
                    }
                }
            }
        }
    }
}