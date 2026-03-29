package com.example.nextcartapp.presentation.ui.meal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.nextcartapp.databinding.LayoutMealTypeBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MealTypeBottomSheet : BottomSheetDialogFragment() {

    private var _binding: LayoutMealTypeBottomSheetBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AddMealViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = LayoutMealTypeBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Se l'utente ha già selezionato un tipo, lo pre-selezioniamo nel gruppo
        val currentType = viewModel.uiState.value.type
        if (currentType != null) {
            for (i in 0 until binding.chipGroupMealType.childCount) {
                val chip = binding.chipGroupMealType.getChildAt(i) as Chip
                if (chip.text == currentType) {
                    chip.isChecked = true
                    break
                }
            }
        }

        // Listener per la selezione della Chip
        binding.chipGroupMealType.setOnCheckedStateChangeListener { group, checkedIds ->
            if (checkedIds.isNotEmpty()) {
                val selectedChip = group.findViewById<Chip>(checkedIds.first())
                val typeValue = selectedChip.text.toString()

                // Aggiorniamo il ViewModel
                viewModel.updateType(typeValue)

                // Chiudiamo dopo una piccola pausa per dare feedback visivo del click
                view.postDelayed({ dismiss() }, 200)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}