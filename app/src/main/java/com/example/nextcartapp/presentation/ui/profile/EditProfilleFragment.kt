package com.example.nextcartapp.presentation.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.nextcartapp.databinding.FragmentEditProfileBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupClickListeners()
        loadUserData()
        observeProfile()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupClickListeners() {
        binding.btnEditBasicInfo.setOnClickListener {
            showEditBasicInfoDialog()
        }

        binding.btnEditEmail.setOnClickListener {
            showEditEmailDialog()
        }

        binding.btnEditPassword.setOnClickListener {
            showResetPasswordDialog()
        }
    }

    private fun loadUserData() {
        // TODO: Caricare dati dal ViewModel/SessionManager
        binding.tvFirstName.text = "Anna"
        binding.tvLastName.text = "Bianchi"
        binding.tvBirthDate.text = "01/01/2000"
        binding.tvEmail.text = "anna.bianchi@gmail.com"
    }

    private fun showEditBasicInfoDialog() {
        val user = viewModel.uiState.value.user
        if (user != null) {
            val dialog = EditBasicInfoBottomSheet.newInstance(
                firstName = user.name,
                lastName = user.surname ?: "",
                birthDate = formatDate(user.dateOfBirth),
                gender = user.gender,
                placeOfBirth = "",  // Non abbiamo placeOfBirth nel backend
                address = user.address ?: ""
            )
            dialog.show(childFragmentManager, "EditBasicInfo")
        }
    }

    private fun showEditEmailDialog() {
        val user = viewModel.uiState.value.user
        if (user != null) {
            val dialog = EditEmailBottomSheet.newInstance(user.email)
            dialog.show(childFragmentManager, "EditEmail")
        }
    }

    private fun showResetPasswordDialog() {
        val user = viewModel.uiState.value.user
        if (user != null) {
            val dialog = ResetPasswordBottomSheet.newInstance(user.email)
            dialog.show(childFragmentManager, "ResetPassword")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeProfile() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    state.user?.let { user ->
                        binding.tvFirstName.text = user.name
                        binding.tvLastName.text = user.surname ?: "-"

                        // Formatta data di nascita (da ISO a dd/MM/yyyy)
                        val formattedDate = formatDate(user.dateOfBirth)
                        binding.tvBirthDate.text = formattedDate

                        binding.tvEmail.text = user.email
                    }
                }
            }
        }
    }

    private fun formatDate(isoDate: String): String {
        return try {
            val date = java.time.LocalDate.parse(isoDate.substring(0, 10))
            java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy").format(date)
        } catch (e: Exception) {
            isoDate.substring(0, 10)
        }
    }
}