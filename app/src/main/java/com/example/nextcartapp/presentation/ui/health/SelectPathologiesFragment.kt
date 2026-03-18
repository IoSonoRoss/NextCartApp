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
import com.example.nextcartapp.databinding.FragmentSelectPathologiesBinding
import com.example.nextcartapp.domain.usecase.health.FilterHealthConditionsUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SelectPathologiesFragment : Fragment() {

    private var _binding: FragmentSelectPathologiesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HealthViewModel by activityViewModels()

    @Inject
    lateinit var filterHealthConditionsUseCase: FilterHealthConditionsUseCase

    private val selectedPathologies = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectPathologiesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        loadPathologies()
        setupSaveButton()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun loadPathologies() {
        viewLifecycleOwner.lifecycleScope.launch {
            // Filtra per categoryCode = "pathology"
            when (val result = filterHealthConditionsUseCase("pathology")) {
                is com.example.nextcartapp.core.util.Result.Success -> {
                    val pathologies = result.data

                    // Crea CheckBox dinamicamente
                    binding.llPathologies.removeAllViews()

                    pathologies.forEach { condition ->
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
                                    selectedPathologies.add(conditionId)
                                } else {
                                    selectedPathologies.remove(conditionId)
                                }
                            }
                        }
                        binding.llPathologies.addView(checkBox)
                    }

                    // Pre-seleziona pathologies già salvate
                    val currentlySelected = viewModel.newHealthConditionState.value.selectedPathologies
                    currentlySelected.forEach { selectedId ->
                        val checkBox = binding.llPathologies.findViewWithTag<CheckBox>(selectedId)
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
            viewModel.setPathologies(selectedPathologies)
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