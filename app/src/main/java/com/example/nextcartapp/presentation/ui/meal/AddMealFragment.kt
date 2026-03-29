package com.example.nextcartapp.presentation.ui.meal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.nextcartapp.R
import com.example.nextcartapp.databinding.FragmentAddMealBinding
import com.example.nextcartapp.presentation.ui.profile.ProfileViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Locale
import java.text.SimpleDateFormat
import java.util.Date

@AndroidEntryPoint
class AddMealFragment : Fragment() {

    private var _binding: FragmentAddMealBinding? = null
    private val binding get() = _binding!!

    // Usiamo activityViewModels per condividere i dati con i vari Bottom Sheets
    private val viewModel: AddMealViewModel by activityViewModels()
    private val profileViewModel: ProfileViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAddMealBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupStepListeners()
        observeViewModelState()

        binding.btnSaveMeal.setOnClickListener {
            val userId = profileViewModel.getUserId() ?: 1
            viewModel.saveMeal(userId)
        }
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupStepListeners() {
        // Step 1: Nome
        binding.stepName.root.setOnClickListener {
            val sheet = MealNameBottomSheet()
            sheet.show(childFragmentManager, "MealNameBottomSheet")
        }

        // Step 2: Tipo (Lo implementeremo tra poco)
        binding.stepType.root.setOnClickListener {
            if (it.isEnabled) { // Ricorda che è sbloccato solo se lo step Nome è ok
                val typeSheet = MealTypeBottomSheet()
                typeSheet.show(childFragmentManager, "MealTypeBottomSheet")
            }
        }

        // Step 3: Data
        binding.stepDate.root.setOnClickListener {
            if (it.isEnabled) {
                showDatePicker()
            }
        }

        // Step 4: Calorie (Opzionale)
        binding.stepKcal.root.setOnClickListener {
            if (it.isEnabled) MealKcalBottomSheet().show(childFragmentManager, "KcalSheet")
        }
    }

    private fun observeViewModelState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    updateUI(state)
                }
            }
        }
    }

    private fun updateUI(state: AddMealUiState) {
        // Aggiornamento STEP 1 (Nome)
        binding.stepName.tvStepTitle.text = state.name ?: "Inserisci Nome del Pasto"
        updateStepStatus(binding.stepName.ivStepStatus, state.name != null)

        // Aggiornamento STEP 2 (Tipo) - Sbloccato solo se Step 1 è ok
        val isNameSet = state.name != null
        binding.stepType.root.isEnabled = isNameSet
        binding.stepType.root.alpha = if (isNameSet) 1f else 0.5f
        binding.stepType.tvStepTitle.text = state.type ?: "Seleziona Tipo Pasto"
        updateStepStatus(binding.stepType.ivStepStatus, state.type != null)

        // Aggiornamento STEP 3 (Data) - Sbloccato solo se Step 2 è ok
        val isTypeSet = state.type != null
        binding.stepDate.root.isEnabled = isTypeSet
        binding.stepDate.root.alpha = if (isTypeSet) 1f else 0.5f
        binding.stepDate.tvStepTitle.text = state.date ?: "Seleziona Data e Ora"
        updateStepStatus(binding.stepDate.ivStepStatus, state.date != null)

        // Aggiornamento STEP 4 (Kcal) - Sempre accessibile se i precedenti sono ok
        val isDateSet = state.date != null
        binding.stepKcal.root.isEnabled = isDateSet
        binding.stepKcal.root.alpha = if (isDateSet) 1f else 0.5f
        binding.stepKcal.tvStepTitle.text = if (state.kcal != null) "${state.kcal} kcal" else "Calorie (Opzionale)"
        updateStepStatus(binding.stepKcal.ivStepStatus, state.kcal != null)

        // Abilitazione pulsante Salva
        binding.btnSaveMeal.isEnabled = isNameSet && isTypeSet && isDateSet

        // Gestione Successo o Errore
        if (state.isSuccess) {
            Toast.makeText(context, "Pasto registrato con successo!", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp() // Torna al diario
        }

        state.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            viewModel.resetError()
        }
    }

    private fun showDatePicker() {
        // 1. Inizializzazione del Builder per il DatePicker
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Seleziona data del pasto")
            // Imposta la data di oggi come predefinita
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()

        // 2. Listener per la conferma della data
        datePicker.addOnPositiveButtonClickListener { selection ->
            // Convertiamo i millisecondi in una stringa leggibile (es: 29/03/2026)
            val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val dateString = formatter.format(Date(selection))

            // Aggiorniamo il ViewModel
            viewModel.updateDate(dateString)
        }

        // 3. Mostriamo il componente
        datePicker.show(childFragmentManager, "DATE_PICKER_TAG")
    }

    private fun updateStepStatus(imageView: android.widget.ImageView, isCompleted: Boolean) {
        if (isCompleted) {
            imageView.setImageResource(R.drawable.ic_check_circle)
            imageView.setColorFilter(resources.getColor(R.color.md_theme_primary, null))
        } else {
            imageView.setImageResource(R.drawable.ic_chevron_right)
            imageView.setColorFilter(resources.getColor(R.color.md_theme_onSurfaceVariant, null))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}