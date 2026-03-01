package com.example.nextcartapp.presentation.ui.wellness

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.nextcartapp.R
import com.example.nextcartapp.databinding.FragmentWellnessBinding

class WellnessFragment : Fragment() {

    private var _binding: FragmentWellnessBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWellnessBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    findNavController().navigate(R.id.homeFragment)
                    true
                }
                R.id.nav_profile -> {
                    findNavController().navigate(R.id.profileFragment)
                    true
                }
                R.id.nav_lifestyle -> {
                    // Già qui
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