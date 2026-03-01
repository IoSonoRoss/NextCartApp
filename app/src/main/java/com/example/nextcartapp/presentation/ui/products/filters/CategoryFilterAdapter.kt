package com.example.nextcartapp.presentation.ui.products.filters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.nextcartapp.databinding.ItemFilterCategoryBinding
import com.example.nextcartapp.domain.model.ProductCategory

data class CategoryItem(
    val category: ProductCategory,
    val isSelected: Boolean
)

class CategoryFilterAdapter(
    private val onItemChecked: (ProductCategory, Boolean) -> Unit
) : ListAdapter<CategoryItem, CategoryFilterAdapter.ViewHolder>(CategoryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFilterCategoryBinding.inflate(
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
        private val binding: ItemFilterCategoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CategoryItem) {
            binding.checkbox.text = item.category.category
            binding.checkbox.isChecked = item.isSelected

            binding.checkbox.setOnCheckedChangeListener { _, isChecked ->
                onItemChecked(item.category, isChecked)
            }

            binding.root.setOnClickListener {
                binding.checkbox.isChecked = !binding.checkbox.isChecked
            }
        }
    }

    private class CategoryDiffCallback : DiffUtil.ItemCallback<CategoryItem>() {
        override fun areItemsTheSame(oldItem: CategoryItem, newItem: CategoryItem): Boolean {
            return oldItem.category.productCategoryId == newItem.category.productCategoryId
        }

        override fun areContentsTheSame(oldItem: CategoryItem, newItem: CategoryItem): Boolean {
            return oldItem == newItem
        }
    }
}