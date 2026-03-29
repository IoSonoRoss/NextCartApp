package com.example.nextcartapp.presentation.ui.cart

import CartManageAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nextcartapp.R
import com.example.nextcartapp.databinding.FragmentCartListBinding
import com.example.nextcartapp.domain.model.Cart
import com.example.nextcartapp.presentation.ui.profile.ProfileViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CartListFragment : Fragment() {

    private var _binding: FragmentCartListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CartViewModel by viewModels()
    private val profileViewModel: ProfileViewModel by activityViewModels()

    private lateinit var cartAdapter: CartManageAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCartListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentUserId = profileViewModel.getUserId() ?: 1

        // Inizializzazione Adapter
        cartAdapter = CartManageAdapter(
            onCartClick = { cart ->
                val bundle = Bundle().apply {
                    putInt("cartId", cart.cartId)
                }
                findNavController().navigate(R.id.action_cartList_to_cartDetails, bundle)
            },
            onDeleteClick = { cart ->
                showDeleteConfirmation(cart, currentUserId)
            }
        )

        // Impostiamo sia l'adapter che il LayoutManager da codice per sicurezza
        binding.rvCartList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCartList.adapter = cartAdapter

        // Osservazione dello stato
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    Log.d("CART_DEBUG", "Stato: isLoading=${state.isLoading}, Size=${state.carts.size}")

                    binding.progressBar.isVisible = state.isLoading

                    // Invio lista all'adapter
                    cartAdapter.submitList(state.carts)

                    // Logica Empty State
                    val isListEmpty = !state.isLoading && state.carts.isEmpty()
                    binding.emptyState.isVisible = isListEmpty
                    binding.rvCartList.isVisible = !isListEmpty

                    state.error?.let {
                        Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                        viewModel.resetActionState()
                    }
                }
            }
        }

        binding.fabCreateCart.setOnClickListener {
            showCreateCartDialog(currentUserId)
        }

        // Chiamata dati
        viewModel.loadUserCarts(currentUserId)
    }

    private fun showDeleteConfirmation(cart: Cart, userId: Int) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Elimina Carrello")
            .setMessage("Vuoi eliminare '${cart.name}'?")
            .setPositiveButton("Elimina") { _, _ ->
                viewModel.deleteCart(userId, cart.cartId)
            }
            .setNegativeButton("Annulla", null)
            .show()
    }

    private fun showCreateCartDialog(userId: Int) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_create_cart, null)
        val etName = dialogView.findViewById<TextInputEditText>(R.id.etCartName)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Nuovo Carrello")
            .setView(dialogView)
            .setPositiveButton("Crea") { _, _ ->
                val name = etName.text.toString()
                if (name.isNotBlank()) {
                    viewModel.createCart(userId, name)
                }
            }
            .setNegativeButton("Annulla", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}