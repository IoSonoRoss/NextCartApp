package com.example.nextcartapp.presentation.ui.auth.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.nextcartapp.databinding.FragmentRegisterStep4Binding
import com.example.nextcartapp.presentation.ui.auth.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterStep4Fragment : Fragment() {

    private var _binding: FragmentRegisterStep4Binding? = null
    private val binding get() = _binding!!

    private val viewModel: RegisterViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterStep4Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupInputListeners()
        setupClickListeners()
        observeUiState()
    }

    private fun setupInputListeners() {
        binding.etPassword.doOnTextChanged { text, _, _, _ ->
            viewModel.onPasswordChanged(text.toString())
        }
        binding.etConfirmPassword.doOnTextChanged { text, _, _, _ ->
            viewModel.onConfirmPasswordChanged(text.toString())
        }
    }

    private fun setupClickListeners() {
        binding.btnNext.setOnClickListener {
            if (viewModel.validateStep4()) {
                viewModel.register()
            }
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.tilPassword.error = state.passwordError
                    binding.tilConfirmPassword.error = state.confirmPasswordError
                    binding.btnNext.isEnabled = !state.isLoading
                    binding.btnNext.text = if (state.isLoading) "..." else "Next"
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}