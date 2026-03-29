package com.example.nextcartapp.presentation.ui.meal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.activityViewModels
import com.example.nextcartapp.databinding.LayoutMealNameBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MealNameBottomSheet : BottomSheetDialogFragment() {

    private var _binding: LayoutMealNameBottomSheetBinding? = null
    private val binding get() = _binding!!

    // Usiamo activityViewModels per condividere lo stato con AddMealFragment
    private val viewModel: AddMealViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = LayoutMealNameBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Mostra automaticamente la tastiera quando si apre il sheet
        binding.etMealName.requestFocus()
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)

        // 2. Pre-popola il campo se l'utente aveva già inserito qualcosa
        binding.etMealName.setText(viewModel.uiState.value.name)

        // 3. Listener per la conferma
        binding.btnConfirmName.setOnClickListener {
            val name = binding.etMealName.text.toString()
            if (name.isNotBlank()) {
                viewModel.updateName(name)
                dismiss() // Chiude il sheet e torna alla schermata hub
            } else {
                binding.tilMealName.error = "Inserisci un nome valido"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}