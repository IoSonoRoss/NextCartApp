package com.example.nextcartapp.presentation.ui.bodycomposition

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
import com.example.nextcartapp.databinding.FragmentAddBodyCompositionBinding
import com.example.nextcartapp.presentation.ui.profile.ProfileViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddBodyCompositionFragment : Fragment() {

    private var _binding: FragmentAddBodyCompositionBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BodyCompositionViewModel by activityViewModels()
    private val profileViewModel: ProfileViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBodyCompositionBinding.inflate(inflater, container, false)
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
            viewModel.resetNewComposition()
            findNavController().navigateUp()
        }
    }

    private fun setupClickListeners() {
        binding.clSelectDateTime.setOnClickListener {
            findNavController().navigate(R.id.action_addBodyComposition_to_selectDateTime)
        }

        binding.clSelectWeight.setOnClickListener {
            findNavController().navigate(R.id.action_addBodyComposition_to_selectWeight)
        }

        binding.clSelectHeight.setOnClickListener {
            findNavController().navigate(R.id.action_addBodyComposition_to_selectHeight)
        }

        binding.btnCancel.setOnClickListener {
            viewModel.resetNewComposition()
            findNavController().navigateUp()
        }

        binding.btnSave.setOnClickListener {
            saveBodyComposition()
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.newCompositionState.collect { state ->
                    updateUI(state)
                }
            }
        }
    }

    private fun updateUI(state: NewBodyCompositionState) {
        if (state.dateTime != null) {
            binding.tvSelectDateTime.text = "Date: ${formatDateTime(state.dateTime)}"
        } else {
            binding.tvSelectDateTime.text = "Select Date and Time"
        }

        if (state.weight != null) {
            binding.tvSelectWeight.text = "Weight: ${state.weight} Kg"
        } else {
            binding.tvSelectWeight.text = "Select Weight"
        }

        if (state.height != null) {
            binding.tvSelectHeight.text = "Height: ${state.height} cm"
        } else {
            binding.tvSelectHeight.text = "Select Height"
        }

        val canSave = state.dateTime != null && state.weight != null && state.height != null
        binding.llActionButtons.isVisible = canSave
    }

    private fun formatDateTime(isoDateTime: String): String {
        return try {
            val parts = isoDateTime.split(" ")
            if (parts.size == 2) {
                val dateParts = parts[0].split("-")
                if (dateParts.size == 3) {
                    "${dateParts[2]}/${dateParts[1]}/${dateParts[0]} ${parts[1]}"
                } else {
                    isoDateTime
                }
            } else {
                isoDateTime
            }
        } catch (e: Exception) {
            isoDateTime
        }
    }

    private fun saveBodyComposition() {
        val userId = profileViewModel.getUserId() ?: 1
        viewModel.saveBodyComposition(userId)
        Snackbar.make(binding.root, "Body composition saved!", Snackbar.LENGTH_SHORT).show()
        findNavController().navigateUp()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}