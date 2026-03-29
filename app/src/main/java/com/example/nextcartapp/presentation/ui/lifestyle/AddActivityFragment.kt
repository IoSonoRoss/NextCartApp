package com.example.nextcartapp.presentation.ui.lifestyle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.nextcartapp.R
import com.example.nextcartapp.databinding.FragmentAddActivityBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddActivityFragment : Fragment() {

    private var _binding: FragmentAddActivityBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LifestyleViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddActivityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupClickListeners()
        setupBottomNavigation()
        observeState()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            viewModel.resetNewActivity()
            findNavController().navigateUp()
        }
    }

    private fun setupClickListeners() {
        // Step 1: Always clickable
        binding.clSelectActivityType.setOnClickListener {
            findNavController().navigate(R.id.action_addActivity_to_selectActivityType)
        }

        // Step 2: Clickable solo se step 1 completato
        binding.clSelectSpecificActivity.setOnClickListener {
            if (viewModel.newActivityState.value.activityType != null) {
                findNavController().navigate(R.id.action_addActivity_to_selectSpecificActivity)
            }
        }

        // Step 3: Clickable solo se step 2 completato
        binding.clSelectDateTime.setOnClickListener {
            if (viewModel.newActivityState.value.specificActivity != null) {
                findNavController().navigate(R.id.action_addActivity_to_selectDateTime)
            }
        }

        // Action Buttons
        binding.btnCancel.setOnClickListener {
            viewModel.resetNewActivity()
            findNavController().navigateUp()
        }

        binding.btnSave.setOnClickListener {
            saveActivity()
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.newActivityState.collect { state ->
                    android.util.Log.d("ADD_ACTIVITY", "State updated: $state")
                    updateUI(state)
                }
            }
        }
    }

    private fun updateUI(state: NewActivityState) {
        // Step 1: Activity Type
        if (state.activityType != null) {
            binding.tvSelectActivityType.text = state.activityType
        } else {
            binding.tvSelectActivityType.text = "Select Activity Type"
        }

        // Step 2: Specific Activity (sblocca se step 1 completato)
        val step2Enabled = state.activityType != null
        binding.clSelectSpecificActivity.isClickable = step2Enabled
        binding.clSelectSpecificActivity.isFocusable = step2Enabled
        binding.clSelectSpecificActivity.alpha = if (step2Enabled) 1.0f else 0.5f

        if (state.specificActivity != null) {
            binding.tvSelectSpecificActivity.text = state.specificActivity
        } else {
            binding.tvSelectSpecificActivity.text = "Select Specific Activity"
        }

        // Step 3: Date & Duration (sblocca se step 2 completato)
        val step3Enabled = state.specificActivity != null
        binding.clSelectDateTime.isClickable = step3Enabled
        binding.clSelectDateTime.isFocusable = step3Enabled
        binding.clSelectDateTime.alpha = if (step3Enabled) 1.0f else 0.5f

        if (state.date != null && state.durationMinutes != null) {
            binding.tvSelectDateTime.text = "${state.date} - ${state.durationMinutes} min"
        } else {
            binding.tvSelectDateTime.text = "Select Date & Duration"
        }

        // Action Buttons (mostra se step 3 completato)
        val allStepsCompleted = state.activityType != null &&
                state.specificActivity != null &&
                state.date != null &&
                state.durationMinutes != null

        binding.llActionButtons.isVisible = allStepsCompleted
    }

    private fun saveActivity() {
        viewModel.saveActivity()
        Snackbar.make(binding.root, "Activity saved!", Snackbar.LENGTH_SHORT).show()
        findNavController().navigate(R.id.lifestyleFragment)
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
                    findNavController().navigate(R.id.nav_cart)
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