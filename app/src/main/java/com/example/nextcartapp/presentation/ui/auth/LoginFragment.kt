package com.example.nextcartapp.presentation.ui.auth

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.text.set
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.nextcartapp.R
import com.example.nextcartapp.databinding.FragmentLoginBinding
import android.text.style.ForegroundColorSpan
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRegisterText()
        setupInputListeners()
        setupClickListeners()
        observeUiState()
        observeUiEvents()
    }

    private fun setupRegisterText() {
        val fullText = "Non hai un account? Registrati"
        val spannable = SpannableString(fullText)
        val startIndex = fullText.indexOf("Registrati")
        val endIndex = startIndex + "Registrati".length
        spannable[startIndex, endIndex] = ForegroundColorSpan(
            ContextCompat.getColor(requireContext(), R.color.md_theme_primary)
        )
        binding.tvRegister.text = spannable
    }

    private fun setupInputListeners() {
        binding.etEmail.doOnTextChanged { text, _, _, _ ->
            viewModel.onEmailChanged(text.toString())
        }
        binding.etPassword.doOnTextChanged { text, _, _, _ ->
            viewModel.onPasswordChanged(text.toString())
        }
    }

    private fun setupClickListeners() {
        binding.btnLogin.setOnClickListener {
            viewModel.login()
        }
        binding.tvRegister.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_register)
        }
        binding.tvForgotPassword.setOnClickListener {
            // TODO: implementare recupero password
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.progressBar.isVisible = state.isLoading
                    binding.btnLogin.isEnabled = !state.isLoading
                    binding.tilEmail.error = state.emailError
                    binding.tilPassword.error = state.passwordError
                    if (state.generalError != null) {
                        binding.tvError.isVisible = true
                        binding.tvError.text = state.generalError
                    } else {
                        binding.tvError.isVisible = false
                    }
                }
            }
        }
    }

    private fun observeUiEvents() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiEvent.collect { event ->
                    when (event) {
                        is LoginViewModel.LoginUiEvent.NavigateToHome -> {
                            findNavController().navigate(R.id.action_login_to_home)
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