package com.example.nextcartapp.presentation.ui.cart

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nextcartapp.core.util.Result
import com.example.nextcartapp.domain.model.Cart
import com.example.nextcartapp.domain.usecase.cart.AddProductToCartUseCase
import com.example.nextcartapp.domain.usecase.cart.CreateCartUseCase
import com.example.nextcartapp.domain.usecase.cart.DeleteCartUseCase
import com.example.nextcartapp.domain.usecase.cart.GetCartUseCase
import com.example.nextcartapp.domain.usecase.cart.CheckoutUseCase // Nuovo import
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
    private val deleteCartUseCase: DeleteCartUseCase,
    private val checkoutUseCase: CheckoutUseCase // Iniettato qui
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
     * Esegue il checkout del carrello: i prodotti vengono spostati in dispensa nel backend.
     */
    fun checkout(cartId: Int, userId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = checkoutUseCase(cartId)) {
                is Result.Success -> {
                    // Dopo il checkout, il carrello potrebbe essere rimosso o cambiato di stato
                    // Ricarichiamo la lista per sincronizzare la UI
                    loadUserCarts(userId)
                    _uiState.update { it.copy(isLoading = false, actionSuccess = true) }
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(isLoading = false, error = "Errore durante il completamento della spesa")
                    }
                }
            }
        }
    }

    /**
     * Aggiunge un prodotto a un carrello già esistente.
     */
    fun addProductToSelectedCart(userId: Int, cartId: Int, productId: String) {
        Log.d("CART_DEBUG", "Tentativo aggiunta: Prodotto $productId in Carrello $cartId")
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = addProductToCartUseCase(cartId, productId)) {
                is Result.Success -> {
                    // FONDAMENTALE: Ricarichiamo i carrelli dal server per avere la lista aggiornata
                    loadUserCarts(userId)
                    _uiState.update { it.copy(actionSuccess = true) }
                }
                is Result.Error -> {
                    _uiState.update { it.copy(isLoading = false, error = "Errore nell'aggiunta") }
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
            _uiState.update { it.copy(isLoading = true) }
            when (val createResult = createCartUseCase(userId, cartName)) {
                is Result.Success -> {
                    val newCartId = createResult.data.cartId
                    when (val addResult = addProductToCartUseCase(newCartId, productId)) {
                        is Result.Success -> {
                            // REFRESH ANCHE QUI
                            loadUserCarts(userId)
                            _uiState.update { it.copy(actionSuccess = true) }
                        }
                        is Result.Error -> { _uiState.update { it.copy(isLoading = false) } }
                    }
                }
                is Result.Error -> { _uiState.update { it.copy(isLoading = false) } }
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