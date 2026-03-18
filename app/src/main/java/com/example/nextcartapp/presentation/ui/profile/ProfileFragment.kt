package com.example.nextcartapp.presentation.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.nextcartapp.R
import com.example.nextcartapp.core.session.SessionManager
import com.example.nextcartapp.databinding.FragmentProfileBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var sessionManager: SessionManager

    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.tvEditDetails.setOnClickListener {
            findNavController().navigate(R.id.action_profile_to_editProfile)
        }

        binding.clOptionHealth.setOnClickListener {
            findNavController().navigate(R.id.action_profile_to_manageHealth)
        }

        setupBottomNavigation()
        setupLogout()
        observeProfile()
    }

    private fun setupLogout() {
        binding.btnLogout.setOnClickListener {
            // Mostra dialog di conferma
            showLogoutConfirmationDialog()
        }
    }

    private fun showLogoutConfirmationDialog() {
        com.google.android.material.dialog.MaterialAlertDialogBuilder(requireContext())
            .setTitle("Logout")
            .setMessage("Sei sicuro di voler uscire?")
            .setPositiveButton("Esci") { _, _ ->
                performLogout()
            }
            .setNegativeButton("Annulla", null)
            .show()
    }

    private fun performLogout() {
        viewLifecycleOwner.lifecycleScope.launch {
            // Cancella la sessione
            sessionManager.clearSession()

            // Naviga al login e rimuovi tutto il back stack
            findNavController().navigate(
                R.id.action_profile_to_login,
                null,
                NavOptions.Builder()
                    .setPopUpTo(R.id.nav_graph, true)
                    .build()
            )
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    findNavController().navigate(R.id.homeFragment)
                    true
                }
                R.id.nav_profile -> {
                    // Già qui
                    true
                }
                R.id.nav_lifestyle -> {
                    findNavController().navigate(R.id.lifestyleFragment)
                    true
                }
                R.id.nav_products -> {
                    findNavController().navigate(R.id.productsFragment)
                    true
                }
                R.id.nav_scan -> {
                    findNavController().navigate(R.id.scannerFragment)
                    true
                }
                else -> false
            }
        }
    }

    private fun observeProfile() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    state.user?.let { user ->
                        val fullName = "${user.name} ${user.surname ?: ""}".trim()
                        binding.tvProfileName.text = fullName

                        // Calcola età dalla data di nascita
                        val age = calculateAge(user.dateOfBirth)
                        binding.tvProfileAge.text = if (age > 0) "Age: $age" else "Age: -"
                    }

                    if (state.error != null) {
                        Snackbar.make(binding.root, state.error, Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun calculateAge(dateOfBirth: String): Int {
        return try {
            val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
            val birthDate = sdf.parse(dateOfBirth.substring(0, 10)) ?: return 0

            val birthCalendar = java.util.Calendar.getInstance().apply {
                time = birthDate
            }

            val today = java.util.Calendar.getInstance()

            var age = today.get(java.util.Calendar.YEAR) - birthCalendar.get(java.util.Calendar.YEAR)

            if (today.get(java.util.Calendar.DAY_OF_YEAR) < birthCalendar.get(java.util.Calendar.DAY_OF_YEAR)) {
                age--
            }

            age
        } catch (e: Exception) {
            0
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}