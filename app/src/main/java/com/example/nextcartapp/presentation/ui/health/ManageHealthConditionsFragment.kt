package com.example.nextcartapp.presentation.ui.health

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
import com.example.nextcartapp.databinding.FragmentManageHealthConditionsBinding
import com.example.nextcartapp.presentation.ui.profile.ProfileViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ManageHealthConditionsFragment : Fragment() {

    private var _binding: FragmentManageHealthConditionsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HealthViewModel by viewModels()
    private val profileViewModel: ProfileViewModel by activityViewModels()
    private lateinit var adapter: HealthConditionAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentManageHealthConditionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupRecyclerView()
        setupFab()
        observeHealthConditions()
        loadUserHealthConditions()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupRecyclerView() {
        adapter = HealthConditionAdapter(
            onDeleteClick = { condition ->
                val userId = profileViewModel.getUserId() ?: 1
                viewModel.removeHealthCondition(userId, condition.healthConditionId)
            }
        )

        binding.rvHealthConditions.layoutManager = LinearLayoutManager(requireContext())
        binding.rvHealthConditions.adapter = adapter
    }

    private fun setupFab() {
        binding.fabAdd.setOnClickListener {
            findNavController().navigate(R.id.action_manageHealth_to_addHealth)
        }
    }

    private fun loadUserHealthConditions() {
        val userId = profileViewModel.getUserId() ?: 1
        viewModel.loadUserHealthConditions(userId)
    }

    private fun observeHealthConditions() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    if (state.userHealthConditions.isEmpty() && !state.isLoading) {
                        showEmptyState()
                    } else {
                        showHealthConditionsList(state.userHealthConditions)
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
        binding.rvHealthConditions.isVisible = false
    }

    private fun showHealthConditionsList(conditions: List<com.example.nextcartapp.domain.model.HealthCondition>) {
        binding.emptyState.isVisible = false
        binding.rvHealthConditions.isVisible = true
        adapter.submitList(conditions)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}