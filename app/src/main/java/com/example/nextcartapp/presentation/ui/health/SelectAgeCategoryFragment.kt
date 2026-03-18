package com.example.nextcartapp.presentation.ui.health

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.nextcartapp.R
import com.example.nextcartapp.databinding.FragmentSelectAgeCategoryBinding
import com.example.nextcartapp.domain.usecase.health.FilterHealthConditionsUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SelectAgeCategoryFragment : Fragment() {

    private var _binding: FragmentSelectAgeCategoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HealthViewModel by activityViewModels()

    @Inject
    lateinit var filterHealthConditionsUseCase: FilterHealthConditionsUseCase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectAgeCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        loadAgeCategories()
        setupRadioGroup()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun loadAgeCategories() {
        viewLifecycleOwner.lifecycleScope.launch {
            android.util.Log.d("SELECT_AGE", "Loading age categories...")

            // Filtra per categoryCode = "age"
            when (val result = filterHealthConditionsUseCase("age")) {
                is com.example.nextcartapp.core.util.Result.Success -> {
                    val ageCategories = result.data
                    android.util.Log.d("SELECT_AGE", "Loaded ${ageCategories.size} age categories")

                    // Crea RadioButton dinamicamente
                    binding.rgAgeCategories.removeAllViews()

                    ageCategories.forEach { condition ->
                        android.util.Log.d("SELECT_AGE", "Adding: ${condition.description}")
                        val radioButton = RadioButton(requireContext()).apply {
                            id = View.generateViewId()
                            text = condition.description
                            tag = condition.healthConditionId
                            setPadding(0, dpToPx(12), 0, dpToPx(12))
                            textSize = 16f
                        }
                        binding.rgAgeCategories.addView(radioButton)
                    }
                }
                is com.example.nextcartapp.core.util.Result.Error -> {
                    android.util.Log.e("SELECT_AGE", "Error loading age categories: ${result.exception.asUiText()}")
                }
            }
        }
    }

    private fun setupRadioGroup() {
        binding.rgAgeCategories.setOnCheckedChangeListener { _, checkedId ->
            val selectedRadioButton = view?.findViewById<RadioButton>(checkedId)
            val ageCategoryId = selectedRadioButton?.tag as? String

            if (ageCategoryId != null) {
                viewModel.setAgeCategory(ageCategoryId)
                findNavController().navigateUp()
            }
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