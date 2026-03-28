package com.example.nextcartapp.presentation.ui.bodycomposition

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.nextcartapp.databinding.FragmentSelectWeightBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectWeightFragment : Fragment() {

    private var _binding: FragmentSelectWeightBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BodyCompositionViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectWeightBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupSaveButton()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupSaveButton() {
        binding.btnSave.setOnClickListener {
            val weightText = binding.etWeight.text.toString()
            if (weightText.isNotEmpty()) {
                val weight = weightText.toDoubleOrNull()
                if (weight != null) {
                    viewModel.setWeight(weight)
                    findNavController().navigateUp()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}