package com.example.nextcartapp.presentation.ui.health

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.nextcartapp.databinding.FragmentSelectPhysiologicalStatesBinding
import com.example.nextcartapp.domain.usecase.health.FilterHealthConditionsUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SelectPhysiologicalStatesFragment : Fragment() {

    private var _binding: FragmentSelectPhysiologicalStatesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HealthViewModel by activityViewModels()

    @Inject
    lateinit var filterHealthConditionsUseCase: FilterHealthConditionsUseCase

    private val selectedStates = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectPhysiologicalStatesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        loadPhysiologicalStates()
        setupSaveButton()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun loadPhysiologicalStates() {
        viewLifecycleOwner.lifecycleScope.launch {
            // Filtra per categoryCode = "physiological_state"
            when (val result = filterHealthConditionsUseCase("physiological_state")) {
                is com.example.nextcartapp.core.util.Result.Success -> {
                    val states = result.data

                    // Crea CheckBox dinamicamente
                    binding.llPhysiologicalStates.removeAllViews()

                    states.forEach { condition ->
                        val checkBox = CheckBox(requireContext()).apply {
                            id = View.generateViewId()
                            text = condition.description
                            tag = condition.healthConditionId
                            setPadding(0, dpToPx(12), 0, dpToPx(12))
                            textSize = 16f

                            // Listener per aggiornare la lista
                            setOnCheckedChangeListener { _, isChecked ->
                                val conditionId = tag as String
                                if (isChecked) {
                                    selectedStates.add(conditionId)
                                } else {
                                    selectedStates.remove(conditionId)
                                }
                            }
                        }
                        binding.llPhysiologicalStates.addView(checkBox)
                    }

                    // Pre-seleziona stati già salvati
                    val currentlySelected = viewModel.newHealthConditionState.value.selectedPhysiologicalStates
                    currentlySelected.forEach { selectedId ->
                        val checkBox = binding.llPhysiologicalStates.findViewWithTag<CheckBox>(selectedId)
                        checkBox?.isChecked = true
                    }
                }
                is com.example.nextcartapp.core.util.Result.Error -> {
                    // Gestisci errore
                }
            }
        }
    }

    private fun setupSaveButton() {
        binding.btnSave.setOnClickListener {
            viewModel.setPhysiologicalStates(selectedStates)
            findNavController().navigateUp()
        }
    }

    private fun dpToPx(dp: Int): Int {
        val density = resources.displayMetrics.density
        return (dp * density).toInt()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}