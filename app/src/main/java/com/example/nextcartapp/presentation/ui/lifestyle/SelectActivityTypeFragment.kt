package com.example.nextcartapp.presentation.ui.lifestyle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.nextcartapp.R
import com.example.nextcartapp.databinding.FragmentSelectActivityTypeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectActivityTypeFragment : Fragment() {

    private var _binding: FragmentSelectActivityTypeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LifestyleViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectActivityTypeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupRadioGroup()
        setupBottomNavigation()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupRadioGroup() {
        binding.rgActivityTypes.setOnCheckedChangeListener { _, checkedId ->
            val selectedRadioButton = view?.findViewById<RadioButton>(checkedId)
            val activityType = selectedRadioButton?.text.toString()

            // Salva nel ViewModel
            viewModel.setActivityType(activityType)

            // Torna indietro
            findNavController().navigateUp()
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.selectedItemId = R.id.nav_lifestyle

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    findNavController().navigate(R.id.homeFragment)
                    true
                }
                R.id.nav_cart -> {
                    findNavController().navigate(R.id.cartFragment)
                    true
                }
                R.id.nav_lifestyle -> {
                    findNavController().navigate(R.id.lifestyleFragment)
                    true
                }
                R.id.nav_products -> {
                    findNavController().navigate(R.id.productsFragment)
                    true
                }
                R.id.nav_scan -> {
                    findNavController().navigate(R.id.scannerFragment)
                    true
                }
                else -> false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}