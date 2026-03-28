package com.example.nextcartapp.presentation.ui.bodycomposition

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.nextcartapp.databinding.ItemBodyCompositionBinding
import com.example.nextcartapp.domain.model.BodyComposition

class BodyCompositionAdapter(
    private val onDeleteClick: (BodyComposition) -> Unit
) : ListAdapter<BodyComposition, BodyCompositionAdapter.BodyCompositionViewHolder>(BodyCompositionDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BodyCompositionViewHolder {
        val binding = ItemBodyCompositionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return BodyCompositionViewHolder(binding, onDeleteClick)
    }

    override fun onBindViewHolder(holder: BodyCompositionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class BodyCompositionViewHolder(
        private val binding: ItemBodyCompositionBinding,
        private val onDeleteClick: (BodyComposition) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(composition: BodyComposition) {
            binding.tvTitle.text = "Body Composition"

            // Formatta data da ISO timestamp a dd/MM/yyyy
            val formattedDate = formatDate(composition.measuredAt)
            binding.tvDate.text = "Date: $formattedDate"

            // Formatta peso e altezza
            binding.tvWeight.text = "Weight: ${composition.weight} Kg"
            binding.tvHeight.text = "Height: ${composition.height} cm"

            // Click su delete
            binding.btnDelete.setOnClickListener {
                onDeleteClick(composition)
            }
        }

        private fun formatDate(isoDateTime: String): String {
            return try {
                // Da "2025-12-15T08:30:00.000Z" a "15/12/2025"
                val datePart = isoDateTime.substringBefore("T")
                val parts = datePart.split("-")
                if (parts.size == 3) {
                    "${parts[2]}/${parts[1]}/${parts[0]}"
                } else {
                    isoDateTime
                }
            } catch (e: Exception) {
                isoDateTime
            }
        }
    }

    class BodyCompositionDiffCallback : DiffUtil.ItemCallback<BodyComposition>() {
        override fun areItemsTheSame(oldItem: BodyComposition, newItem: BodyComposition): Boolean {
            return oldItem.measuredAt == newItem.measuredAt && oldItem.consumerId == newItem.consumerId
        }

        override fun areContentsTheSame(oldItem: BodyComposition, newItem: BodyComposition): Boolean {
            return oldItem == newItem
        }
    }
}