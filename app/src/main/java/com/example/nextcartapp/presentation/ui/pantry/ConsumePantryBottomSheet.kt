package com.example.nextcartapp.presentation.ui.pantry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.example.nextcartapp.core.util.ProductUnit
import com.example.nextcartapp.databinding.LayoutConsumePantryBottomSheetBinding
import com.example.nextcartapp.domain.model.PantryItem
import com.example.nextcartapp.presentation.ui.profile.ProfileViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConsumePantryBottomSheet : BottomSheetDialogFragment() {

    // Utilizziamo activityViewModels per sincronizzare i dati con PantryFragment
    private val viewModel: PantryViewModel by activityViewModels()
    private val profileViewModel: ProfileViewModel by activityViewModels()

    private var _binding: LayoutConsumePantryBottomSheetBinding? = null
    private val binding get() = _binding!!

    private var consumeAmount = 1.0f

    companion object {
        private const val ARG_PANTRY_ITEM = "pantry_item_data"

        /**
         * Metodo statico per creare l'istanza del BottomSheet passando l'oggetto PantryItem.
         * Rispetta il ciclo di vita di Android e i requisiti di Hilt.
         */
        fun newInstance(item: PantryItem): ConsumePantryBottomSheet {
            val fragment = ConsumePantryBottomSheet()
            val args = Bundle()
            args.putParcelable(ARG_PANTRY_ITEM, item)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LayoutConsumePantryBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Recupero sicuro dell'item dal Bundle
        val item = arguments?.getParcelable<PantryItem>(ARG_PANTRY_ITEM) ?: return

        // Inizializzazione UI
        binding.tvConsumeTitle.text = "Consuma ${item.product.name}"
        val unitLabel = getUnitLabel(item)
        binding.tvCurrentStock.text = "In dispensa: ${formatQuantity(item.quantity)} $unitLabel"

        setupQuantitySelector(item)

        // Listener tasto conferma
        binding.btnConfirmConsume.setOnClickListener {
            val amountToConsume = if (item.product.unitType == ProductUnit.UNIT) {
                consumeAmount
            } else {
                binding.etAmount.text.toString().toFloatOrNull() ?: 0f
            }

            // Validazione logica
            if (amountToConsume <= 0) {
                Toast.makeText(requireContext(), "Inserisci una quantità valida", Toast.LENGTH_SHORT).show()
            } else if (amountToConsume > item.quantity) {
                Toast.makeText(requireContext(), "Quantità superiore a quella disponibile!", Toast.LENGTH_SHORT).show()
            } else {
                val userId = profileViewModel.getUserId() ?: 1
                viewModel.consumeProduct(userId, item.id, amountToConsume)
                dismiss() // Chiude il BottomSheet al successo
            }
        }
    }

    private fun setupQuantitySelector(item: PantryItem) {
        when (item.product.unitType) {
            ProductUnit.UNIT -> {
                binding.llUnitSelector.isVisible = true
                binding.tvCount.text = "1"
                consumeAmount = 1.0f

                binding.btnAdd.setOnClickListener {
                    if (consumeAmount < item.quantity) {
                        consumeAmount++
                        binding.tvCount.text = consumeAmount.toInt().toString()
                    }
                }
                binding.btnMinus.setOnClickListener {
                    if (consumeAmount > 1) {
                        consumeAmount--
                        binding.tvCount.text = consumeAmount.toInt().toString()
                    }
                }
            }
            ProductUnit.WEIGHT_G, ProductUnit.VOLUME_ML -> {
                binding.tilNumeric.isVisible = true
                binding.tilNumeric.suffixText = getUnitLabel(item)
                // Suggeriamo l'intera quantità o una porzione standard
                binding.etAmount.setText(formatQuantity(item.quantity))
            }
        }
    }

    private fun getUnitLabel(item: PantryItem): String {
        return when (item.product.unitType) {
            ProductUnit.WEIGHT_G -> "g"
            ProductUnit.VOLUME_ML -> "ml"
            ProductUnit.UNIT -> "pz"
        }
    }

    private fun formatQuantity(quantity: Float): String {
        return if (quantity % 1 == 0f) quantity.toInt().toString() else quantity.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}