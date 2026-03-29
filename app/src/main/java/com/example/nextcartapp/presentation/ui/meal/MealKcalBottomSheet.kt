package com.example.nextcartapp.presentation.ui.meal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.activityViewModels
import com.example.nextcartapp.databinding.LayoutMealKcalBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MealKcalBottomSheet : BottomSheetDialogFragment() {

    private var _binding: LayoutMealKcalBottomSheetBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AddMealViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = LayoutMealKcalBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Mostra il valore attuale se presente
        viewModel.uiState.value.kcal?.let {
            binding.etMealKcal.setText(it.toString())
        }

        // 2. Apri automaticamente il tastierino numerico
        binding.etMealKcal.requestFocus()
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)

        // 3. Listener conferma
        binding.btnConfirmKcal.setOnClickListener {
            val kcalString = binding.etMealKcal.text.toString()

            if (kcalString.isNotBlank()) {
                val kcalValue = kcalString.toFloatOrNull()
                if (kcalValue != null) {
                    viewModel.updateKcal(kcalValue)
                    dismiss()
                } else {
                    binding.tilMealKcal.error = "Inserisci un numero valido"
                }
            } else {
                // Se è vuoto, lo resettiamo a null (essendo opzionale)
                viewModel.updateKcal(null)
                dismiss()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}