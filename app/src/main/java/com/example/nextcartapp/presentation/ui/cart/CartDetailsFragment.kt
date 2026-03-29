package com.example.nextcartapp.presentation.ui.cart

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nextcartapp.databinding.FragmentCartDetailsBinding
import com.example.nextcartapp.presentation.ui.profile.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CartDetailsFragment : Fragment() {
    private var _binding: FragmentCartDetailsBinding? = null
    private val binding get() = _binding!!

    // ViewModel condiviso con tutta l'Activity
    private val viewModel: CartViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCartDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cartId = arguments?.getInt("cartId") ?: -1

        // Setup Adapter e LayoutManager
        val adapter = CartDetailsAdapter()
        binding.rvProductList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvProductList.adapter = adapter

        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    // Cerchiamo il carrello specifico
                    val currentCart = state.carts.find { it.cartId == cartId }

                    currentCart?.let {
                        binding.toolbar.title = it.name
                        adapter.submitList(it.items)

                        // DEBUG LOG
                        Log.d("CART_DEBUG", "Dettaglio: Carrello ${it.name} ha ${it.items.size} prodotti")
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}