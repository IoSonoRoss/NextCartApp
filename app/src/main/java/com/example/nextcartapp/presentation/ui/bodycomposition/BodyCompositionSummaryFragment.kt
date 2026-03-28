package com.example.nextcartapp.presentation.ui.bodycomposition

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.example.nextcartapp.databinding.FragmentBodyCompositionSummaryBinding
import com.example.nextcartapp.presentation.ui.profile.ProfileViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BodyCompositionSummaryFragment : Fragment() {

    private var _binding: FragmentBodyCompositionSummaryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BodyCompositionViewModel by viewModels()
    private val profileViewModel: ProfileViewModel by activityViewModels()
    private lateinit var adapter: BodyCompositionAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBodyCompositionSummaryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupRecyclerView()
        setupFab()
        observeBodyCompositions()
        loadBodyCompositions()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupRecyclerView() {
        adapter = BodyCompositionAdapter(
            onDeleteClick = { composition ->
                val userId = profileViewModel.getUserId() ?: 1
                viewModel.deleteBodyComposition(userId, composition.measuredAt)
            }
        )

        binding.rvBodyCompositions.layoutManager = LinearLayoutManager(requireContext())
        binding.rvBodyCompositions.adapter = adapter
    }

    private fun setupFab() {
        binding.fabAdd.setOnClickListener {
            findNavController().navigate(R.id.action_bodyCompositionSummary_to_addBodyComposition)
        }
    }

    private fun loadBodyCompositions() {
        val userId = profileViewModel.getUserId() ?: 1
        viewModel.loadBodyCompositions(userId)
    }

    private fun observeBodyCompositions() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    if (state.compositions.isEmpty() && !state.isLoading) {
                        showEmptyState()
                    } else {
                        showCompositionsList(state.compositions)
                    }

                    if (state.error != null) {
                        Snackbar.make(binding.root, state.error, Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun showEmptyState() {
        binding.emptyState.isVisible = true
        binding.rvBodyCompositions.isVisible = false
    }

    private fun showCompositionsList(compositions: List<com.example.nextcartapp.domain.model.BodyComposition>) {
        binding.emptyState.isVisible = false
        binding.rvBodyCompositions.isVisible = true
        adapter.submitList(compositions)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}