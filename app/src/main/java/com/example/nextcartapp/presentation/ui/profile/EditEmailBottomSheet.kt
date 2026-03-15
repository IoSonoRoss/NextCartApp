package com.example.nextcartapp.presentation.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.nextcartapp.databinding.BottomSheetEditEmailBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class EditEmailBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomSheetEditEmailBinding? = null
    private val binding get() = _binding!!

    companion object {
        private const val ARG_EMAIL = "email"

        fun newInstance(email: String): EditEmailBottomSheet {
            return EditEmailBottomSheet().apply {
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
        _binding = BottomSheetEditEmailBinding.inflate(inflater, container, false)
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

        binding.btnSave.setOnClickListener {
            saveEmail()
        }
    }

    private fun loadCurrentData() {
        arguments?.getString(ARG_EMAIL)?.let { email ->
            binding.etEmail.setText(email)
        }
    }

    private fun saveEmail() {
        val email = binding.etEmail.text.toString()

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.error = "Invalid email"
            return
        }

        // TODO: Salvare tramite ViewModel

        dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}