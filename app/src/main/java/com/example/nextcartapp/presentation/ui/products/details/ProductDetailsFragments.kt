package com.example.nextcartapp.presentation.ui.products.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.nextcartapp.R
import com.example.nextcartapp.databinding.FragmentProductDetailsBinding
import com.example.nextcartapp.domain.model.ProductDetails
import com.example.nextcartapp.presentation.ui.cart.CartViewModel
import com.example.nextcartapp.presentation.ui.cart.SelectCartBottomSheet
import com.example.nextcartapp.presentation.ui.profile.ProfileViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductDetailsFragment : Fragment() {

    private var _binding: FragmentProductDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProductDetailsViewModel by viewModels()
    private val cartViewModel: CartViewModel by activityViewModels()
    private val profileViewModel: ProfileViewModel by activityViewModels()

    private val args: ProductDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupExpandableCards()
        setupAddToCartListener()
        observeUiState()

        viewModel.loadProductDetails(args.productId)
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupExpandableCards() {
        binding.headerDiets.setOnClickListener { toggleCard(binding.tvDiets, binding.iconDiets) }
        binding.headerNutritional.setOnClickListener { toggleCard(binding.tvNutritional, binding.iconNutritional) }
        binding.headerClaims.setOnClickListener { toggleCard(binding.tvClaims, binding.iconClaims) }
        binding.headerAllergens.setOnClickListener { toggleCard(binding.tvAllergens, binding.iconAllergens) }
    }

    private fun setupAddToCartListener() {
        binding.btnAddToCart.setOnClickListener {
            val productDetails = viewModel.uiState.value.productDetails
            val userState = profileViewModel.uiState.value // Controlla il nome di questa classe

            if (productDetails != null) {

                // --- LOGICA DI CONTROLLO (MODIFICATA PER ESSERE PIÙ SICURA) ---

                // 1. Controlla Allergeni
                // NOTA: Se 'allergies' non esiste nel tuo ProfileUiState, cambialo con il nome corretto (es. healthConditions)
                // Se gli elementi della lista sono stringhe, rimuovi ".description"
                val allergenConflicts = productDetails.allergens.filter { productAllergen ->
                    // Esempio: se userState ha una lista di stringhe chiamata 'userAllergies'
                    // userState.userAllergies.contains(productAllergen)
                    false // Temporaneo per far compilare: sostituisci con la tua lista
                }

                // 2. Controlla Diete
                val dietConflicts = listOf<String>() // Temporaneo: sostituisci con la logica simile a sopra

                if (allergenConflicts.isNotEmpty() || dietConflicts.isNotEmpty()) {
                    showCompatibilityAlert(productDetails, allergenConflicts, dietConflicts)
                } else {
                    openQuantityBottomSheet(productDetails)
                }
            }
        }
    }

    private fun toggleCard(contentView: View, iconView: View) {
        if (contentView.isVisible) {
            contentView.visibility = View.GONE
            iconView.rotation = 0f
        } else {
            contentView.visibility = View.VISIBLE
            iconView.rotation = 180f
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    state.productDetails?.let { product ->
                        binding.tvProductName.text = product.itName ?: product.name
                        binding.tvCategory.text = product.categoryName ?: "Categoria non disponibile"

                        binding.tvDiets.text = if (product.diets.isEmpty()) "Nessuna dieta specificata"
                        else product.diets.joinToString("\n") { "• $it" }

                        binding.tvNutritional.text = if (product.nutritionalValues.isEmpty()) "Valori nutrizionali non disponibili"
                        else product.nutritionalValues.joinToString("\n") { nv ->
                            "• ${nv.name}: ${nv.value ?: 0} ${nv.unit}"
                        }

                        binding.tvClaims.text = if (product.claims.isEmpty()) "Nessun claim disponibile"
                        else product.claims.joinToString("\n") { "• $it" }

                        binding.tvAllergens.text = if (product.allergens.isEmpty()) "Nessun allergene dichiarato"
                        else product.allergens.joinToString("\n") { "• $it" }
                    }

                    state.error?.let {
                        Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun showCompatibilityAlert(product: ProductDetails, allergens: List<String>, diets: List<String>) {
        val message = StringBuilder("Attenzione!\n\n")
        if (allergens.isNotEmpty()) message.append("⚠️ ALLERGENI: ${allergens.joinToString(", ")}.\n\n")
        if (diets.isNotEmpty()) message.append("🚫 DIETA: Non compatibile con le tue scelte.\n\n")
        message.append("Vuoi procedere comunque?")

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Conflitto Alimentare")
            .setMessage(message.toString())
            .setPositiveButton("Sì, aggiungi") { _, _ -> openQuantityBottomSheet(product) }
            .setNegativeButton("Annulla", null)
            .show()
    }

    private fun openQuantityBottomSheet(details: ProductDetails) {
        val productForSheet = com.example.nextcartapp.domain.model.Product(
            productId = details.productId,
            name = details.name,
            unitType = details.unitType,
            defaultPackageSize = details.defaultPackageSize,
            itName = details.itName,
            categoryName = details.categoryName,
            imageUrl = details.imageUrl
        )
        val bottomSheet = SelectCartBottomSheet.newInstance(productForSheet)
        bottomSheet.show(parentFragmentManager, "SelectCartBottomSheet")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}