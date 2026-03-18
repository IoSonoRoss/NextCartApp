package com.example.nextcartapp.presentation.ui.health

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
import com.example.nextcartapp.R
import com.example.nextcartapp.databinding.FragmentAddHealthConditionBinding
import com.example.nextcartapp.presentation.ui.profile.ProfileViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddHealthConditionFragment : Fragment() {

    private var _binding: FragmentAddHealthConditionBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HealthViewModel by activityViewModels()
    private val profileViewModel: ProfileViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddHealthConditionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupClickListeners()
        observeState()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            viewModel.resetNewHealthCondition()
            findNavController().navigateUp()
        }
    }

    private fun setupClickListeners() {
        binding.clSelectAge.setOnClickListener {
            findNavController().navigate(R.id.action_addHealth_to_selectAge)
        }

        binding.clSelectPathologies.setOnClickListener {
            if (viewModel.newHealthConditionState.value.selectedAgeCategory != null) {
                findNavController().navigate(R.id.action_addHealth_to_selectPathologies)
            }
        }

        binding.clSelectPhysiological.setOnClickListener {
            if (viewModel.newHealthConditionState.value.selectedAgeCategory != null) {
                findNavController().navigate(R.id.action_addHealth_to_selectPhysiological)
            }
        }

        binding.btnCancel.setOnClickListener {
            viewModel.resetNewHealthCondition()
            findNavController().navigateUp()
        }

        binding.btnSave.setOnClickListener {
            saveHealthConditions()
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.newHealthConditionState.collect { state ->
                    updateUI(state)
                }
            }
        }
    }

    private fun updateUI(state: NewHealthConditionState) {
        if (state.selectedAgeCategory != null) {
            binding.tvSelectAge.text = "Age: Selected"
        } else {
            binding.tvSelectAge.text = "Select Age Category"
        }

        val step2Enabled = state.selectedAgeCategory != null
        binding.clSelectPathologies.isClickable = step2Enabled
        binding.clSelectPathologies.isFocusable = step2Enabled
        binding.clSelectPathologies.alpha = if (step2Enabled) 1.0f else 0.5f

        if (state.selectedPathologies.isNotEmpty()) {
            binding.tvSelectPathologies.text = "Pathologies: ${state.selectedPathologies.size} selected"
        } else {
            binding.tvSelectPathologies.text = "Select Pathologies (compatible with age)"
        }

        val step3Enabled = state.selectedAgeCategory != null
        binding.clSelectPhysiological.isClickable = step3Enabled
        binding.clSelectPhysiological.isFocusable = step3Enabled
        binding.clSelectPhysiological.alpha = if (step3Enabled) 1.0f else 0.5f

        if (state.selectedPhysiologicalStates.isNotEmpty()) {
            binding.tvSelectPhysiological.text = "Physiological: ${state.selectedPhysiologicalStates.size} selected"
        } else {
            binding.tvSelectPhysiological.text = "Select Physiological States (compatible with age)"
        }

        val canSave = state.selectedAgeCategory != null
        binding.llActionButtons.isVisible = canSave
    }

    private fun saveHealthConditions() {
        val userId = profileViewModel.getUserId() ?: 1
        viewModel.saveHealthConditions(userId)
        Snackbar.make(binding.root, "Health conditions saved!", Snackbar.LENGTH_SHORT).show()
        findNavController().navigateUp()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}