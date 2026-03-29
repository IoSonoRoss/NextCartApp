package com.example.nextcartapp.presentation.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.nextcartapp.databinding.FragmentCartDetailsBinding
import com.example.nextcartapp.presentation.ui.profile.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CartDetailsFragment : Fragment() {

    private var _binding: FragmentCartDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CartViewModel by viewModels()
    private val profileViewModel: ProfileViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCartDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cartId = arguments?.getInt("cartId") ?: -1
        val userId = profileViewModel.getUserId() ?: 1

        val adapter = CartDetailsAdapter()
        binding.rvProductList.adapter = adapter

        setupToolbar()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    // Troviamo il carrello specifico tra quelli caricati
                    val currentCart = state.carts.find { it.cartId == cartId }
                    currentCart?.let {
                        binding.toolbar.title = it.name
                        adapter.submitList(it.items)
                    }
                }
            }
        }

        binding.btnCheckout.setOnClickListener {
            Toast.makeText(requireContext(), "Funzionalità di pagamento in arrivo!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}