package com.example.nextcartapp.presentation.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.nextcartapp.R
import com.example.nextcartapp.domain.model.Cart

class HomeCartAdapter(private val onCartClick: (Cart) -> Unit) :
    androidx.recyclerview.widget.ListAdapter<Cart, HomeCartAdapter.ViewHolder>(CartDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_home_cart, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position), onCartClick)

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(cart: Cart, onClick: (Cart) -> Unit) {
            // Usiamo itemView invece di v
            itemView.findViewById<android.widget.TextView>(R.id.tvCartName).text = cart.name
            itemView.setOnClickListener { onClick(cart) }
        }
    }

    class CartDiffCallback : androidx.recyclerview.widget.DiffUtil.ItemCallback<Cart>() {
        override fun areItemsTheSame(old: Cart, new: Cart) = old.cartId == new.cartId
        override fun areContentsTheSame(old: Cart, new: Cart) = old == new
    }
}