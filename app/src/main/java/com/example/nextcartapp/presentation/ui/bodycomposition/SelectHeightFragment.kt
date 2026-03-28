package com.example.nextcartapp.presentation.ui.bodycomposition

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.nextcartapp.databinding.FragmentSelectHeightBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectHeightFragment : Fragment() {

    private var _binding: FragmentSelectHeightBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BodyCompositionViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectHeightBinding.inflate(inflater, container, false)
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
            val heightText = binding.etHeight.text.toString()
            if (heightText.isNotEmpty()) {
                val height = heightText.toDoubleOrNull()
                if (height != null) {
                    viewModel.setHeight(height)
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