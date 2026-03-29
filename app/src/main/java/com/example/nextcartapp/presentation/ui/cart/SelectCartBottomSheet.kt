package com.example.nextcartapp.presentation.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nextcartapp.R
import com.example.nextcartapp.databinding.DialogCreateCartBinding
import com.example.nextcartapp.databinding.LayoutSelectCartBottomSheetBinding
import com.example.nextcartapp.presentation.ui.profile.ProfileViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SelectCartBottomSheet : BottomSheetDialogFragment() {

    private val viewModel: CartViewModel by activityViewModels()
    private val profileViewModel: ProfileViewModel by activityViewModels()

    private var _binding: LayoutSelectCartBottomSheetBinding? = null
    private val binding get() = _binding!!

    // Recuperiamo il productId dagli Arguments (Best Practice)
    private val productId: String by lazy {
        arguments?.getString(ARG_PRODUCT_ID) ?: ""
    }

    companion object {
        private const val ARG_PRODUCT_ID = "product_id"

        fun newInstance(productId: String): SelectCartBottomSheet {
            val fragment = SelectCartBottomSheet()
            val args = Bundle()
            args.putString(ARG_PRODUCT_ID, productId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LayoutSelectCartBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Recupero ID Utente reale dal ProfileViewModel
        val currentUserId = profileViewModel.getUserId() ?: 1

        // 2. Setup RecyclerView
        val adapter = CartSelectionAdapter { cart ->
            val userId = profileViewModel.getUserId() ?: 1
            viewModel.addProductToSelectedCart(userId, cart.cartId, productId)
        }
        binding.rvCarts.adapter = adapter
        binding.rvCarts.layoutManager = LinearLayoutManager(requireContext())

        // 3. Listener per creazione nuovo carrello
        binding.clCreateNewCart.setOnClickListener {
            showCreateCartDialog(currentUserId)
        }

        // 4. Osservazione dello stato del ViewModel (Uso repeatOnLifecycle per sicurezza)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    // Aggiorna la lista dei carrelli
                    adapter.submitList(state.carts)

                    // Gestione successo azione
                    if (state.actionSuccess) {
                        Toast.makeText(requireContext(), "Prodotto aggiunto correttamente!", Toast.LENGTH_SHORT).show()
                        dismiss()
                    }

                    // Gestione eventuali errori
                    state.error?.let {
                        Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                        viewModel.resetActionState() // Reset dopo aver mostrato l'errore
                    }
                }
            }
        }

        // 5. Caricamento iniziale dei carrelli per l'utente corrente
        viewModel.loadUserCarts(userId = currentUserId)
    }

    private fun showCreateCartDialog(userId: Int) {
        val dialogBinding = DialogCreateCartBinding.inflate(LayoutInflater.from(requireContext()))

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Nuovo Carrello")
            .setView(dialogBinding.root)
            .setPositiveButton("Crea e Aggiungi") { _, _ ->
                val name = dialogBinding.etCartName.text.toString()
                if (name.isNotBlank()) {
                    viewModel.createCartAndAddProduct(
                        userId = userId,
                        cartName = name,
                        productId = productId
                    )
                } else {
                    Toast.makeText(requireContext(), "Inserisci un nome valido", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Annulla", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.resetActionState() // Pulizia dello stato prima della chiusura
        _binding = null
    }
}