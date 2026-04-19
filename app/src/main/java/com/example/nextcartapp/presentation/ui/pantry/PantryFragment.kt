package com.example.nextcartapp.presentation.ui.pantry

import android.os.Bundle
import android.view.View
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nextcartapp.databinding.FragmentPantryBinding
import com.example.nextcartapp.presentation.ui.profile.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PantryFragment : Fragment() {

    private var _binding: FragmentPantryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PantryViewModel by viewModels()
    private val profileViewModel: ProfileViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val userId = profileViewModel.getUserId() ?: 1

        val adapter = PantryAdapter()
        binding.rvPantry.adapter = adapter
        binding.rvPantry.layoutManager = LinearLayoutManager(requireContext())

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.progressBar.isVisible = state.isLoading
                    adapter.submitList(state.items)
                }
            }
        }

        viewModel.loadPantry(userId)
    }
}