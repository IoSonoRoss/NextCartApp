package com.example.nextcartapp.presentation.ui.products.filters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.nextcartapp.R
import com.example.nextcartapp.databinding.FragmentFiltersSelectionBinding

class FiltersSelectionFragment : Fragment() {

    private var _binding: FragmentFiltersSelectionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFiltersSelectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnCategories.setOnClickListener {
            findNavController().navigate(R.id.action_filtersSelection_to_selectCategories)
        }

        binding.btnDiets.setOnClickListener {
            findNavController().navigate(R.id.action_filtersSelection_to_selectDiets)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}