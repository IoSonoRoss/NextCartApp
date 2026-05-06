package com.example.nextcartapp.presentation.ui.pantry

import android.os.Bundle
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
import com.example.nextcartapp.databinding.FragmentPantryBinding
import com.example.nextcartapp.presentation.ui.profile.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import com.example.nextcartapp.presentation.ui.pantry.ConsumePantryBottomSheet

@AndroidEntryPoint
class PantryFragment : Fragment() {

    private var _binding: FragmentPantryBinding? = null
    private val binding get() = _binding!!

    // Usiamo activityViewModels per condividere lo stato della dispensa con il BottomSheet di consumo
    private val viewModel: PantryViewModel by activityViewModels()
    private val profileViewModel: ProfileViewModel by activityViewModels()

    // Modifichiamo l'inizializzazione dell'adapter per includere il click listener
    private lateinit var pantryAdapter: PantryAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPantryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Inizializzazione dell'Adapter con la logica del click
        pantryAdapter = PantryAdapter { selectedItem ->
            // 2. Chiama newInstance invece del costruttore
            val bottomSheet = ConsumePantryBottomSheet.newInstance(selectedItem)
            bottomSheet.show(childFragmentManager, "ConsumePantryBottomSheet")
        }

        // 2. Setup RecyclerView
        binding.rvPantry.apply {
            adapter = pantryAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        observeUiState()

        // 3. Caricamento iniziale dei dati
        val currentUserId = profileViewModel.getUserId() ?: 1
        viewModel.loadPantry(currentUserId)
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.progressBar.isVisible = state.isLoading

                    // Gestione Lista ed Empty State
                    pantryAdapter.submitList(state.items)

                    val showEmpty = !state.isLoading && state.items.isEmpty()
                    binding.emptyState.isVisible = showEmpty
                    binding.rvPantry.isVisible = !showEmpty

                    state.error?.let {
                        Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
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