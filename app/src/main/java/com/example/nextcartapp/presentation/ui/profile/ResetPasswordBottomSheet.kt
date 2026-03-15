package com.example.nextcartapp.presentation.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.nextcartapp.databinding.BottomSheetResetPasswordBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar

class ResetPasswordBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomSheetResetPasswordBinding? = null
    private val binding get() = _binding!!

    companion object {
        private const val ARG_EMAIL = "email"

        fun newInstance(email: String): ResetPasswordBottomSheet {
            return ResetPasswordBottomSheet().apply {
                arguments = Bundle().apply {
                    putString(ARG_EMAIL, email)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetResetPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()
        loadCurrentData()
    }

    private fun setupClickListeners() {
        binding.ivClose.setOnClickListener {
            dismiss()
        }

        binding.tvConfirm.setOnClickListener {
            sendResetEmail()
        }
    }

    private fun loadCurrentData() {
        arguments?.getString(ARG_EMAIL)?.let { email ->
            binding.etEmail.setText(email)
        }
    }

    private fun sendResetEmail() {
        val email = binding.etEmail.text.toString()

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.error = "Invalid email"
            return
        }

        // TODO: Chiamare API per reset password
        Snackbar.make(binding.root, "Email di reset inviata a $email", Snackbar.LENGTH_LONG).show()

        dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}