package com.example.nextcartapp.presentation.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.nextcartapp.R
import com.example.nextcartapp.databinding.ItemProductBinding
import com.example.nextcartapp.domain.model.Product

class ProductsAdapter(
    private val onProductClick: (Product) -> Unit
) : ListAdapter<Product, ProductsAdapter.ViewHolder>(ProductDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding, onProductClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: ItemProductBinding,
        private val onProductClick: (Product) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            binding.tvProductTitle.text = product.itName ?: product.name ?: "Prodotto"

            // Se hai un URL immagine, caricalo con Coil
            if (product.imageUrl != null) {
                binding.ivProduct.load(product.imageUrl) {
                    placeholder(R.drawable.placeholder_image)
                    error(R.drawable.placeholder_image)
                }
            } else {
                binding.ivProduct.setImageResource(R.drawable.placeholder_image)
            }

            binding.root.setOnClickListener {
                onProductClick(product)
            }
        }
    }

    private class ProductDiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean =
            oldItem.productId == newItem.productId

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean =
            oldItem == newItem
    }
}