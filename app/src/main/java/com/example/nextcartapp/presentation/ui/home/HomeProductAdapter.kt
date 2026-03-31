package com.example.nextcartapp.presentation.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.nextcartapp.Product
import com.example.nextcartapp.R

class HomeProductAdapter(private val onProductClick: (Product) -> Unit) :
    androidx.recyclerview.widget.ListAdapter<Product, HomeProductAdapter.ViewHolder>(ProductDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_home_small, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position), onProductClick)

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(product: Product, onClick: (Product) -> Unit) {
            itemView.findViewById<android.widget.TextView>(R.id.tvItemLabel).text = product.name
            itemView.setOnClickListener { onClick(product) }
        }
    }

    class ProductDiffCallback : androidx.recyclerview.widget.DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(old: Product, new: Product) = old.id == new.id
        override fun areContentsTheSame(old: Product, new: Product) = old == new
    }
}