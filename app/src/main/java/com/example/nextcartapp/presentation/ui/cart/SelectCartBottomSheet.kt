package com.example.nextcartapp.presentation.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nextcartapp.R
import com.example.nextcartapp.core.util.ProductUnit
import com.example.nextcartapp.databinding.DialogCreateCartBinding
import com.example.nextcartapp.databinding.LayoutSelectCartBottomSheetBinding
import com.example.nextcartapp.domain.model.Product
import com.example.nextcartapp.presentation.ui.profile.ProfileViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SelectCartBottomSheet : BottomSheetDialogFragment() {

    private val viewModel: CartViewModel by activityViewModels()
    private val profileViewModel: ProfileViewModel by activityViewModels()

    private var _binding: LayoutSelectCartBottomSheetBinding? = null
    private val binding get() = _binding!!

    private var currentQuantity: Float = 1.0f

    // Recuperiamo l'intero oggetto Product
    private val product: Product by lazy {
        arguments?.getParcelable<Product>(ARG_PRODUCT) ?: throw IllegalArgumentException("Product missing")
    }

    companion object {
        private const val ARG_PRODUCT = "product_data"

        fun newInstance(product: Product): SelectCartBottomSheet {
            val fragment = SelectCartBottomSheet()
            val args = Bundle()
            args.putParcelable(ARG_PRODUCT, product)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = LayoutSelectCartBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentUserId = profileViewModel.getUserId() ?: 1

        // 1. Configurazione del selettore di quantità in base al tipo di prodotto
        setupQuantityUI()

        // 2. Setup RecyclerView dei carrelli
        val adapter = CartSelectionAdapter { cart ->
            val quantity = getFinalQuantity()
            viewModel.addProductToSelectedCart(currentUserId, cart.cartId, product.productId, quantity)
        }
        binding.rvCarts.adapter = adapter
        binding.rvCarts.layoutManager = LinearLayoutManager(requireContext())

        // 3. Listener per creazione nuovo carrello
        binding.clCreateNewCart.setOnClickListener {
            showCreateCartDialog(currentUserId)
        }

        // 4. Osservazione dello stato
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    adapter.submitList(state.carts)
                    if (state.actionSuccess) {
                        Toast.makeText(requireContext(), "Aggiunto: ${getFinalQuantity()} ${getUnitLabel()}", Toast.LENGTH_SHORT).show()
                        dismiss()
                    }
                    state.error?.let {
                        Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                        viewModel.resetActionState()
                    }
                }
            }
        }

        viewModel.loadUserCarts(userId = currentUserId)
    }

    private fun setupQuantityUI() {
        when (product.unitType) {
            ProductUnit.UNIT -> {
                binding.llUnitSelector.isVisible = true
                binding.tvUnitCount.text = "1"
                currentQuantity = 1.0f

                binding.btnAdd.setOnClickListener {
                    currentQuantity++
                    binding.tvUnitCount.text = currentQuantity.toInt().toString()
                }
                binding.btnMinus.setOnClickListener {
                    if (currentQuantity > 1) {
                        currentQuantity--
                        binding.tvUnitCount.text = currentQuantity.toInt().toString()
                    }
                }
            }
            ProductUnit.WEIGHT_G, ProductUnit.VOLUME_ML -> {
                binding.tilNumericInput.isVisible = true
                binding.tilNumericInput.suffixText = if (product.unitType == ProductUnit.WEIGHT_G) "g" else "ml"

                // Pre-compila con il valore di default (es. 500g) o un valore standard
                val defaultValue = product.defaultPackageSize ?: 500f
                binding.etNumericValue.setText(defaultValue.toInt().toString())
            }
        }
    }

    private fun getFinalQuantity(): Float {
        return if (product.unitType == ProductUnit.UNIT) {
            currentQuantity
        } else {
            binding.etNumericValue.text.toString().toFloatOrNull() ?: 1.0f
        }
    }

    private fun getUnitLabel(): String {
        return when(product.unitType) {
            ProductUnit.UNIT -> "pz"
            ProductUnit.WEIGHT_G -> "g"
            ProductUnit.VOLUME_ML -> "ml"
        }
    }

    private fun showCreateCartDialog(userId: Int) {
        val dialogBinding = DialogCreateCartBinding.inflate(LayoutInflater.from(requireContext()))
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Nuovo Carrello")
            .setView(dialogBinding.root)
            .setPositiveButton("Crea e Aggiungi") { _, _ ->
                val name = dialogBinding.etCartName.text.toString()
                if (name.isNotBlank()) {
                    viewModel.createCartAndAddProduct(
                        userId = userId,
                        cartName = name,
                        productId = product.productId,
                        quantity = getFinalQuantity() // Passiamo la quantità anche qui!
                    )
                }
            }
            .setNegativeButton("Annulla", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.resetActionState()
        _binding = null
    }
}