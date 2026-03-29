package com.example.nextcartapp.presentation.ui.products.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.nextcartapp.R
import com.example.nextcartapp.databinding.FragmentProductDetailsBinding
import com.example.nextcartapp.presentation.ui.cart.SelectCartBottomSheet
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductDetailsFragment : Fragment() {

    private var _binding: FragmentProductDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProductDetailsViewModel by viewModels()
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
        observeUiState()

        viewModel.loadProductDetails(args.productId)
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupExpandableCards() {
        // Diets
        binding.headerDiets.setOnClickListener {
            toggleCard(binding.tvDiets, binding.iconDiets)
        }

        // Nutritional Values
        binding.headerNutritional.setOnClickListener {
            toggleCard(binding.tvNutritional, binding.iconNutritional)
        }

        // Claims
        binding.headerClaims.setOnClickListener {
            toggleCard(binding.tvClaims, binding.iconClaims)
        }

        // Allergens
        binding.headerAllergens.setOnClickListener {
            toggleCard(binding.tvAllergens, binding.iconAllergens)
        }

        binding.btnAddToCart.setOnClickListener {
            // Recuperiamo il prodotto corrente dallo stato del ViewModel dei dettagli
            val currentProduct = viewModel.uiState.value.productDetails

            if (currentProduct != null) {
                // Apriamo il Bottom Sheet passando l'ID del prodotto
                val bottomSheet = SelectCartBottomSheet.newInstance(currentProduct.productId)
                bottomSheet.show(parentFragmentManager, "SelectCartBottomSheet")
            } else {
                Toast.makeText(requireContext(), "Dati prodotto non pronti", Toast.LENGTH_SHORT).show()
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
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->

                    state.productDetails?.let { product ->
                        binding.tvProductName.text = product.itName ?: product.name
                        binding.tvCategory.text = product.categoryName ?: "Categoria non disponibile"

                        // Diets
                        binding.tvDiets.text = if (product.diets.isEmpty()) {
                            "Nessuna dieta specificata"
                        } else {
                            product.diets.joinToString("\n") { "• $it" }
                        }

                        // Nutritional Values
                        binding.tvNutritional.text = if (product.nutritionalValues.isEmpty()) {
                            "Valori nutrizionali non disponibili"
                        } else {
                            product.nutritionalValues.joinToString("\n") { nv ->
                                "• ${nv.name}: ${nv.value} ${nv.unit ?: ""}"
                            }
                        }

                        // Claims
                        binding.tvClaims.text = if (product.claims.isEmpty()) {
                            "Nessun claim disponibile"
                        } else {
                            product.claims.joinToString("\n") { "• $it" }
                        }

                        // Allergens
                        binding.tvAllergens.text = if (product.allergens.isEmpty()) {
                            "Nessun allergene dichiarato"
                        } else {
                            product.allergens.joinToString("\n") { "• $it" }
                        }
                    }

                    if (state.error != null) {
                        Snackbar.make(binding.root, state.error, Snackbar.LENGTH_LONG).show()
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