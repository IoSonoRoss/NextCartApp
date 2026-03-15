package com.example.nextcartapp.presentation.ui.lifestyle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.nextcartapp.databinding.FragmentSelectSpecificActivityBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectSpecificActivityFragment : Fragment() {

    private var _binding: FragmentSelectSpecificActivityBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LifestyleViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectSpecificActivityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupRadioGroup()
        loadActivitiesForType()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun loadActivitiesForType() {
        // TODO: Caricare le attività specifiche in base al tipo selezionato
        // Per ora lasciamo la lista hardcoded nel layout
    }

    private fun setupRadioGroup() {
        binding.rgSpecificActivities.setOnCheckedChangeListener { _, checkedId ->
            val selectedRadioButton = view?.findViewById<RadioButton>(checkedId)
            val specificActivity = selectedRadioButton?.text.toString()

            // Per ora usiamo un activityId hardcoded
            // TODO: Mappare correttamente specificActivity -> activityId dal backend
            val activityId = getActivityIdForSpecificActivity(specificActivity)

            // Salva la selezione nel ViewModel
            viewModel.setSpecificActivity(specificActivity, activityId)

            // Torna indietro
            findNavController().navigateUp()
        }
    }

    private fun getActivityIdForSpecificActivity(specificActivity: String): Int {
        // Mapping temporaneo - TODO: sostituire con dati reali dal DB
        return when {
            specificActivity.contains("montagna, in salita") -> 1003
            specificActivity.contains("montagna, corsa") -> 1004
            specificActivity.contains("BMX") -> 1008
            specificActivity.contains("montagna, generale") -> 1009
            else -> 1015 // Default: Ciclismo, generale
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}