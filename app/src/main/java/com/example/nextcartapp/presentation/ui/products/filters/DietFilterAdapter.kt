package com.example.nextcartapp.presentation.ui.products.filters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.nextcartapp.databinding.ItemFilterDietBinding
import com.example.nextcartapp.domain.model.Diet

data class DietItem(
    val diet: Diet,
    val isSelected: Boolean
)

class DietFilterAdapter(
    private val onItemChecked: (Diet, Boolean) -> Unit
) : ListAdapter<DietItem, DietFilterAdapter.ViewHolder>(DietDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFilterDietBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(
        private val binding: ItemFilterDietBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DietItem) {
            binding.checkbox.text = item.diet.description
            binding.checkbox.isChecked = item.isSelected

            binding.checkbox.setOnCheckedChangeListener { _, isChecked ->
                onItemChecked(item.diet, isChecked)
            }

            binding.root.setOnClickListener {
                binding.checkbox.isChecked = !binding.checkbox.isChecked
            }
        }
    }

    private class DietDiffCallback : DiffUtil.ItemCallback<DietItem>() {
        override fun areItemsTheSame(oldItem: DietItem, newItem: DietItem): Boolean {
            return oldItem.diet.dietId == newItem.diet.dietId
        }

        override fun areContentsTheSame(oldItem: DietItem, newItem: DietItem): Boolean {
            return oldItem == newItem
        }
    }
}