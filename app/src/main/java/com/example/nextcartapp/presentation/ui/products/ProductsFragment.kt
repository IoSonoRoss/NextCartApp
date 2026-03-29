package com.example.nextcartapp.presentation.ui.products

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.nextcartapp.R
import com.example.nextcartapp.databinding.FragmentProductsBinding
import com.example.nextcartapp.presentation.common.ProductsAdapter
import com.example.nextcartapp.presentation.ui.products.filters.FiltersViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductsFragment : Fragment() {

    private var _binding: FragmentProductsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProductsViewModel by viewModels()
    private val filtersViewModel: FiltersViewModel by activityViewModels() // ← SHARED ViewModel
    private lateinit var adapter: ProductsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupSearch()
        setupBottomNavigation()  // ← AGGIUNGI
        observeUiState()
        observeFilters()
    }

    private fun setupRecyclerView() {
        adapter = ProductsAdapter { product ->
            // Naviga ai dettagli
            val action = ProductsFragmentDirections.actionProductsToDetails(product.productId)
            findNavController().navigate(action)
        }
        binding.rvProducts.adapter = adapter
    }

    private fun setupSearch() {
        binding.etSearch.addTextChangedListener { text ->
            viewModel.onSearchQueryChanged(text.toString())
        }

        binding.ivFilter.setOnClickListener {
            findNavController().navigate(R.id.action_products_to_filters)
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    adapter.submitList(state.products)
                    setupCategoryChips(state.categories)

                    if (state.error != null) {
                        Snackbar.make(binding.root, state.error, Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun observeFilters() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                filtersViewModel.uiState.collect { filterState ->
                    // Applica i filtri al ViewModel dei prodotti
                    viewModel.applyFilters(
                        categories = filterState.selectedCategories.toList(),
                        diets = filterState.selectedDiets.toList()
                    )
                }
            }
        }
    }

    private fun setupCategoryChips(categories: List<String>) {
        binding.chipGroup.removeAllViews()
        binding.chipGroup.isSingleSelection = true

        val allChip = Chip(requireContext()).apply {
            id = View.generateViewId()
            text = "Tutti"
            isCheckable = true
            isChecked = true
            setOnClickListener {
                viewModel.onCategorySelected(null)
            }
        }
        binding.chipGroup.addView(allChip)

        categories.forEach { category ->
            val chip = Chip(requireContext()).apply {
                id = View.generateViewId()
                text = category
                isCheckable = true
                setOnClickListener {
                    viewModel.onCategorySelected(category)
                }
            }
            binding.chipGroup.addView(chip)
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    findNavController().navigate(R.id.homeFragment)
                    true
                }

                R.id.nav_cart -> {
                    findNavController().navigate(R.id.profileFragment)
                    true
                }

                R.id.nav_lifestyle -> {
                    findNavController().navigate(R.id.lifestyleFragment)
                    true
                }

                R.id.nav_products -> {
                    // Già qui, non fare nulla
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}