package com.example.nextcartapp.presentation.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nextcartapp.R
import com.example.nextcartapp.databinding.FragmentHomeBinding
import com.example.nextcartapp.domain.model.Cart
import com.example.nextcartapp.domain.model.Product
import com.example.nextcartapp.presentation.ui.cart.CartViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    // ViewModel per i dati della dashboard (Nome, Prodotti suggeriti)
    private val viewModel: HomeViewModel by viewModels()

    // ViewModel dei carrelli (usiamo activityViewModels per avere i dati già caricati)
    private val cartViewModel: CartViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerViews()
        setupClickListeners()
        setupBottomNavigation()
        observeUiState()

        // Forza il caricamento dei carrelli all'apertura della home
        // (Sostituisci '1' con l'ID reale se necessario)
        cartViewModel.loadUserCarts(userId = 1)
    }

    private fun setupRecyclerViews() {
        // 1. Setup Favorites (Orizzontale)
        binding.rvFavorites.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = HomeProductAdapter { product ->
                // Naviga al dettaglio prodotto
            }
        }

        // 2. Setup My Carts (Orizzontale)
        binding.rvCarts.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = HomeCartAdapter { cart ->
                val bundle = Bundle().apply { putInt("cartId", cart.cartId) }
                findNavController().navigate(R.id.cartDetailsFragment, bundle)
            }
        }

        // 3. Setup Suggested (Orizzontale)
        binding.rvSuggested.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = HomeProductAdapter { product ->
                // Naviga al dettaglio prodotto
            }
        }
    }

    private fun setupClickListeners() {
        // Profilo e Settings
        binding.ivAccount.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_profile)
        }

        binding.ivSettings.setOnClickListener {
            // findNavController().navigate(R.id.settingsFragment)
        }

        // Frecce per "View All"
        binding.ivFavoritesArrow.setOnClickListener {
            findNavController().navigate(R.id.nav_products)
        }

        binding.ivCartsArrow.setOnClickListener {
            findNavController().navigate(R.id.nav_cart)
        }
    }

    private fun setupBottomNavigation() {
        // Assicuriamoci che l'item corretto sia selezionato
        binding.bottomNavigation.selectedItemId = R.id.nav_home

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> true
                R.id.nav_products -> {
                    findNavController().navigate(R.id.productsFragment)
                    true
                }
                R.id.nav_cart -> {
                    findNavController().navigate(R.id.cartFragment)
                    true
                }
                R.id.nav_lifestyle -> {
                    findNavController().navigate(R.id.lifestyleFragment)
                    true
                }
                R.id.nav_scan -> {
                    findNavController().navigate(R.id.scannerFragment)
                    true
                }
                else -> false
            }
        }
    }

    private fun observeUiState() {
        // Osserva i dati del profilo e prodotti suggeriti
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.tvWelcome.text = "Hello, ${state.userName}!"
                    // Se avessi prodotti suggeriti: (binding.rvSuggested.adapter as HomeProductAdapter).submitList(state.suggested)
                }
            }
        }

        // Osserva i carrelli reali dal CartViewModel
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                cartViewModel.uiState.collect { state ->
                    (binding.rvCarts.adapter as HomeCartAdapter).submitList(state.carts)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}