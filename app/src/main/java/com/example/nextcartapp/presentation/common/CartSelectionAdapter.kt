package com.example.nextcartapp.presentation.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.nextcartapp.R
import com.example.nextcartapp.domain.model.Cart

class CartSelectionAdapter(private val onCartClick: (Cart) -> Unit) :
    ListAdapter<Cart, CartSelectionAdapter.CartViewHolder>(CartDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart_selection, parent, false)
        return CartViewHolder(view, onCartClick)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class CartViewHolder(view: View, val onClick: (Cart) -> Unit) : RecyclerView.ViewHolder(view) {
        private val tvName: TextView = view.findViewById(R.id.tvCartName)
        fun bind(cart: Cart) {
            tvName.text = cart.name
            itemView.setOnClickListener { onClick(cart) }
        }
    }

    class CartDiffCallback : DiffUtil.ItemCallback<Cart>() {
        override fun areItemsTheSame(oldItem: Cart, newItem: Cart) = oldItem.cartId == newItem.cartId
        override fun areContentsTheSame(oldItem: Cart, newItem: Cart) = oldItem == newItem
    }
}