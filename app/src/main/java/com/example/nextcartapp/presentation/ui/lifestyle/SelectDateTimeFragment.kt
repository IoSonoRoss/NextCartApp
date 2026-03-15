package com.example.nextcartapp.presentation.ui.lifestyle

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.nextcartapp.databinding.FragmentSelectDateTimeBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class SelectDateTimeFragment : Fragment() {

    private var _binding: FragmentSelectDateTimeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LifestyleViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectDateTimeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupDatePicker()
        setupSaveButton()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupDatePicker() {
        binding.etSelectDate.setOnClickListener {
            showDatePicker()
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val date = String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear)
                binding.etSelectDate.setText(date)
            },
            year,
            month,
            day
        ).show()
    }

    private fun setupSaveButton() {
        binding.btnSave.setOnClickListener {
            saveDateTime()
        }
    }

    private fun saveDateTime() {
        val date = binding.etSelectDate.text.toString()
        val durationText = binding.etDuration.text.toString()

        if (date.isEmpty()) {
            Snackbar.make(binding.root, "Please select a date", Snackbar.LENGTH_SHORT).show()
            return
        }

        if (durationText.isEmpty() || durationText.toIntOrNull() == null) {
            Snackbar.make(binding.root, "Please enter duration in minutes", Snackbar.LENGTH_SHORT).show()
            return
        }

        val durationMinutes = durationText.toInt()

        // Salva nel ViewModel
        viewModel.setDateTime(date, durationMinutes)

        // Torna indietro
        findNavController().navigateUp()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}