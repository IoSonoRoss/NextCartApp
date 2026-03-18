package com.example.nextcartapp.presentation.ui.health

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.nextcartapp.databinding.ItemHealthConditionsBinding
import com.example.nextcartapp.domain.model.HealthCondition

class HealthConditionAdapter(
    private val onDeleteClick: (HealthCondition) -> Unit
) : ListAdapter<HealthCondition, HealthConditionAdapter.HealthConditionViewHolder>(HealthConditionDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HealthConditionViewHolder {
        val binding = ItemHealthConditionsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HealthConditionViewHolder(binding, onDeleteClick)
    }

    override fun onBindViewHolder(holder: HealthConditionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class HealthConditionViewHolder(
        private val binding: ItemHealthConditionsBinding,
        private val onDeleteClick: (HealthCondition) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(condition: HealthCondition) {
            binding.tvTitle.text = condition.description
            binding.tvCategory.text = condition.category?.label ?: ""

            binding.ivDelete.setOnClickListener {
                onDeleteClick(condition)
            }
        }
    }

    class HealthConditionDiffCallback : DiffUtil.ItemCallback<HealthCondition>() {
        override fun areItemsTheSame(oldItem: HealthCondition, newItem: HealthCondition): Boolean {
            return oldItem.healthConditionId == newItem.healthConditionId
        }

        override fun areContentsTheSame(oldItem: HealthCondition, newItem: HealthCondition): Boolean {
            return oldItem == newItem
        }
    }
}