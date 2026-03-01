package com.example.nextcartapp.presentation.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.nextcartapp.R
import com.example.nextcartapp.databinding.FragmentRegisterBinding
import com.example.nextcartapp.presentation.ui.auth.registration.RegisterPagerAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    val viewModel: RegisterViewModel by viewModels()

    private val stepLabels = listOf(
        "Step 1 of 4: Basic info",
        "Step 2 of 4: Extra info",
        "Step 3 of 4: Contact info",
        "Step 4 of 4: Create password"
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()
        setupToolbar()
        observeUiEvents()
    }

    private fun setupViewPager() {
        val adapter = RegisterPagerAdapter(this)
        binding.viewPager.adapter = adapter
        binding.viewPager.isUserInputEnabled = false // disabilita swipe manuale

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                updateProgress(position)
            }
        })

        updateProgress(0)
    }

    private fun updateProgress(position: Int) {
        val totalSteps = 4
        val progress = ((position + 1).toFloat() / totalSteps * 100).toInt()
        binding.progressIndicator.progress = progress
        binding.tvStepLabel.text = stepLabels[position]
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            if (!goToPreviousStep()) {
                findNavController().navigateUp()
            }
        }
    }

    fun goToNextStep(): Boolean {
        val currentItem = binding.viewPager.currentItem
        return if (currentItem < 3) {
            binding.viewPager.currentItem = currentItem + 1
            true
        } else {
            false
        }
    }

    fun goToPreviousStep(): Boolean {
        val currentItem = binding.viewPager.currentItem
        return if (currentItem > 0) {
            binding.viewPager.currentItem = currentItem - 1
            true
        } else {
            false
        }
    }

    private fun observeUiEvents() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiEvent.collect { event ->
                    when (event) {
                        is RegisterViewModel.RegisterUiEvent.NavigateToLogin -> {
                            Snackbar.make(
                                binding.root,
                                "Registrazione completata! Accedi ora.",
                                Snackbar.LENGTH_LONG
                            ).show()
                            findNavController().navigate(R.id.action_registration_to_home)
                        }
                        is RegisterViewModel.RegisterUiEvent.ShowError -> {
                            Snackbar.make(binding.root, event.message, Snackbar.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}