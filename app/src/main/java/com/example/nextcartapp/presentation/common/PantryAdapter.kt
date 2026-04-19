package com.example.nextcartapp.presentation.ui.pantry

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.nextcartapp.R
import com.example.nextcartapp.core.util.ProductUnit
import com.example.nextcartapp.domain.model.PantryItem

class PantryAdapter : ListAdapter<PantryItem, PantryAdapter.ViewHolder>(PantryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pantry, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvName: TextView = itemView.findViewById(R.id.tvProductName)
        private val tvCategory: TextView = itemView.findViewById(R.id.tvCategory)
        private val tvQuantity: TextView = itemView.findViewById(R.id.tvQuantity)

        fun bind(item: PantryItem) {
            tvName.text = item.product.name
            tvCategory.text = item.product.categoryName ?: "Nessuna categoria"

            // Logica di formattazione intelligente dell'unità di misura
            val unitSuffix = when (item.product.unitType) {
                ProductUnit.WEIGHT_G -> "g"
                ProductUnit.VOLUME_ML -> "ml"
                ProductUnit.UNIT -> "pz"
            }

            /**
             * Formattazione del numero:
             * Se è un numero intero (es. 2.0), mostriamo "2".
             * Se ha decimali (es. 1.5), mostriamo "1.5".
             */
            val formattedQuantity = if (item.quantity % 1 == 0f) {
                item.quantity.toInt().toString()
            } else {
                item.quantity.toString()
            }

            tvQuantity.text = "$formattedQuantity $unitSuffix"
        }
    }

    /**
     * DiffUtil ottimizza l'aggiornamento della lista:
     * invece di ricaricare tutto, cambia solo gli elementi modificati.
     */
    class PantryDiffCallback : DiffUtil.ItemCallback<PantryItem>() {
        override fun areItemsTheSame(oldItem: PantryItem, newItem: PantryItem): Boolean {
            return oldItem.pantryItemId == newItem.pantryItemId
        }

        override fun areContentsTheSame(oldItem: PantryItem, newItem: PantryItem): Boolean {
            return oldItem == newItem
        }
    }
}