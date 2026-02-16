package com.example.nextcartapp.presentation.ui.auth.registration

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.nextcartapp.R
import com.example.nextcartapp.databinding.FragmentRegisterStep2Binding
import com.example.nextcartapp.presentation.ui.auth.RegisterFragment
import com.example.nextcartapp.presentation.ui.auth.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Calendar

@AndroidEntryPoint
class RegisterStep2Fragment : Fragment() {

    private var _binding: FragmentRegisterStep2Binding? = null
    private val binding get() = _binding!!

    private val viewModel: RegisterViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterStep2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupGenderDropdown()
        setupDatePicker()
        setupInputListeners()
        setupClickListeners()
        observeUiState()
    }

    private fun setupGenderDropdown() {
        val genders = listOf("Male", "Female", "Other")
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item_dropdown, genders)
        binding.actvGender.setAdapter(adapter)
        binding.actvGender.setOnItemClickListener { _, _, position, _ ->
            viewModel.onGenderChanged(genders[position])
        }
    }

    private fun setupDatePicker() {
        binding.tilDateOfBirth.setEndIconOnClickListener { showDatePicker() }
        binding.etDateOfBirth.setOnClickListener { showDatePicker() }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val date = "%04d-%02d-%02d".format(year, month + 1, dayOfMonth)
                binding.etDateOfBirth.setText(date)
                viewModel.onDateOfBirthChanged(date)
            },
            calendar.get(Calendar.YEAR) - 18,
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).apply {
            datePicker.maxDate = Calendar.getInstance().also {
                it.add(Calendar.YEAR, -18)
            }.timeInMillis
        }.show()
    }

    private fun setupInputListeners() {
        binding.etPlaceOfBirth.doOnTextChanged { text, _, _, _ ->
            viewModel.onPlaceOfBirthChanged(text.toString())
        }
        binding.etAddress.doOnTextChanged { text, _, _, _ ->
            viewModel.onAddressChanged(text.toString())
        }
    }

    private fun setupClickListeners() {
        binding.btnNext.setOnClickListener {
            if (viewModel.validateStep2()) {
                (parentFragment as? RegisterFragment)?.goToNextStep()
            }
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.tilDateOfBirth.error = state.dateOfBirthError
                    binding.tilGender.error = state.genderError
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}