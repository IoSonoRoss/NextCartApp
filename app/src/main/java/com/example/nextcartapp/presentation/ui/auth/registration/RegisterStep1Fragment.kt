package com.example.nextcartapp.presentation.ui.auth.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.nextcartapp.databinding.FragmentRegisterStep1Binding
import com.example.nextcartapp.presentation.ui.auth.RegisterFragment
import com.example.nextcartapp.presentation.ui.auth.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterStep1Fragment : Fragment() {

    private var _binding: FragmentRegisterStep1Binding? = null
    private val binding get() = _binding!!

    private val viewModel: RegisterViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterStep1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupInputListeners()
        setupClickListeners()
        observeUiState()
    }

    private fun setupInputListeners() {
        binding.etName.doOnTextChanged { text, _, _, _ ->
            viewModel.onNameChanged(text.toString())
        }
        binding.etSurname.doOnTextChanged { text, _, _, _ ->
            viewModel.onSurnameChanged(text.toString())
        }
    }

    private fun setupClickListeners() {
        binding.btnNext.setOnClickListener {
            if (viewModel.validateStep1()) {
                (parentFragment as? RegisterFragment)?.goToNextStep()
            }
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.tilName.error = state.nameError
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}