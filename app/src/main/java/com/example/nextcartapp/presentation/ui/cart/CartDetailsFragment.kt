package com.example.nextcartapp.presentation.ui.cart

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nextcartapp.databinding.FragmentCartDetailsBinding
import com.example.nextcartapp.presentation.ui.profile.ProfileViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CartDetailsFragment : Fragment() {
    private var _binding: FragmentCartDetailsBinding? = null
    private val binding get() = _binding!!

    // ViewModel condiviso con tutta l'Activity per la logica dei carrelli
    private val viewModel: CartViewModel by activityViewModels()

    // ViewModel per recuperare l'ID utente loggato
    private val profileViewModel: ProfileViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCartDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cartId = arguments?.getInt("cartId") ?: -1
        val currentUserId = profileViewModel.getUserId() ?: 1

        // Setup Adapter e LayoutManager
        val adapter = CartDetailsAdapter()
        binding.rvProductList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvProductList.adapter = adapter

        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }

        // Gestione del click sul pulsante Checkout (Procedi al Pagamento)
        binding.btnCheckout.setOnClickListener {
            showCheckoutConfirmation(cartId, currentUserId)
        }

        // Osservazione dello stato del ViewModel
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    // 1. Cerchiamo il carrello specifico nello stato globale
                    val currentCart = state.carts.find { it.cartId == cartId }

                    currentCart?.let { cart ->
                        binding.toolbar.title = cart.name
                        adapter.submitList(cart.items)
                        Log.d("CART_DEBUG", "Dettaglio: Carrello ${cart.name} ha ${cart.items.size} prodotti")
                    }

                    // 2. Gestione successo dell'operazione di Checkout
                    if (state.actionSuccess) {
                        Toast.makeText(context, "Spesa completata! Dispensa aggiornata.", Toast.LENGTH_LONG).show()
                        viewModel.resetActionState() // Importante: resetta per evitare loop
                        findNavController().navigateUp() // Torna alla lista carrelli
                    }

                    // 3. Gestione eventuali errori
                    state.error?.let {
                        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                        viewModel.resetActionState()
                    }
                }
            }
        }
    }

    /**
     * Mostra un dialog di conferma prima di procedere al completamento della spesa.
     */
    private fun showCheckoutConfirmation(cartId: Int, userId: Int) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Conferma Spesa")
            .setMessage("Vuoi confermare l'acquisto? Tutti i prodotti verranno spostati automaticamente nella tua dispensa.")
            .setPositiveButton("Conferma") { _, _ ->
                viewModel.checkout(cartId, userId)
            }
            .setNegativeButton("Annulla", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}