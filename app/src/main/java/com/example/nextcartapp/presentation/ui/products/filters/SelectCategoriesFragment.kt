package com.example.nextcartapp.presentation.ui.products.filters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nextcartapp.databinding.FragmentSelectCategoriesBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SelectCategoriesFragment : Fragment() {

    private var _binding: FragmentSelectCategoriesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FiltersViewModel by activityViewModels()  // ← activityViewModels per condividere con ProductsFragment
    private lateinit var adapter: CategoryFilterAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupRecyclerView()
        setupSaveButton()  // ← AGGIUNTO
        observeUiState()

        viewModel.loadCategories()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupRecyclerView() {
        adapter = CategoryFilterAdapter(
            onItemChecked = { category, isChecked ->
                viewModel.toggleCategory(category.category)
            }
        )
        binding.rvCategories.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCategories.adapter = adapter
    }

    private fun setupSaveButton() {
        binding.btnSave.setOnClickListener {
            // I filtri sono già salvati nel ViewModel shared
            findNavController().navigateUp()
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.progressBar.isVisible = state.isLoading

                    adapter.submitList(
                        state.categories.map { category ->
                            CategoryItem(
                                category = category,
                                isSelected = category.category in state.selectedCategories
                            )
                        }
                    )

                    if (state.error != null) {
                        Snackbar.make(binding.root, state.error, Snackbar.LENGTH_LONG).show()
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