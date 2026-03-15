package com.example.nextcartapp.presentation.ui.profile

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.nextcartapp.databinding.BottomSheetEditBasicInfoBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.Calendar

class EditBasicInfoBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomSheetEditBasicInfoBinding? = null
    private val binding get() = _binding!!

    companion object {
        private const val ARG_FIRST_NAME = "firstName"
        private const val ARG_LAST_NAME = "lastName"
        private const val ARG_BIRTH_DATE = "birthDate"
        private const val ARG_GENDER = "gender"
        private const val ARG_PLACE_OF_BIRTH = "placeOfBirth"
        private const val ARG_ADDRESS = "address"

        fun newInstance(
            firstName: String,
            lastName: String,
            birthDate: String,
            gender: String,
            placeOfBirth: String,
            address: String
        ): EditBasicInfoBottomSheet {
            return EditBasicInfoBottomSheet().apply {
                arguments = Bundle().apply {
                    putString(ARG_FIRST_NAME, firstName)
                    putString(ARG_LAST_NAME, lastName)
                    putString(ARG_BIRTH_DATE, birthDate)
                    putString(ARG_GENDER, gender)
                    putString(ARG_PLACE_OF_BIRTH, placeOfBirth)
                    putString(ARG_ADDRESS, address)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetEditBasicInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupGenderDropdown()
        setupDatePicker()
        setupClickListeners()
        loadCurrentData()
    }

    private fun setupGenderDropdown() {
        val genders = arrayOf("Male", "Female", "Other", "Prefer not to say")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, genders)
        binding.actvGender.setAdapter(adapter)
    }

    private fun setupDatePicker() {
        binding.etBirthDate.setOnClickListener {
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
                binding.etBirthDate.setText(date)
            },
            year,
            month,
            day
        ).show()
    }

    private fun setupClickListeners() {
        binding.ivClose.setOnClickListener {
            dismiss()
        }

        binding.btnSave.setOnClickListener {
            saveBasicInfo()
        }
    }

    private fun loadCurrentData() {
        arguments?.let { args ->
            binding.etFirstName.setText(args.getString(ARG_FIRST_NAME))
            binding.etLastName.setText(args.getString(ARG_LAST_NAME))
            binding.etBirthDate.setText(args.getString(ARG_BIRTH_DATE))
            binding.actvGender.setText(args.getString(ARG_GENDER), false)
            binding.etPlaceOfBirth.setText(args.getString(ARG_PLACE_OF_BIRTH))
            binding.etAddress.setText(args.getString(ARG_ADDRESS))
        }
    }

    private fun saveBasicInfo() {
        val firstName = binding.etFirstName.text.toString()
        val lastName = binding.etLastName.text.toString()
        val birthDate = binding.etBirthDate.text.toString()
        val gender = binding.actvGender.text.toString()
        val placeOfBirth = binding.etPlaceOfBirth.text.toString()
        val address = binding.etAddress.text.toString()

        // TODO: Salvare tramite ViewModel

        dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}