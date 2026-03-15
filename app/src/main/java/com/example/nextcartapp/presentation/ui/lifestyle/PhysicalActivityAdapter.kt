package com.example.nextcartapp.presentation.ui.lifestyle

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.nextcartapp.databinding.ItemActivityBinding
import com.example.nextcartapp.domain.model.PhysicalActivity

class PhysicalActivityAdapter(
    private val onDeleteClick: (PhysicalActivity) -> Unit
) : ListAdapter<PhysicalActivity, PhysicalActivityAdapter.ActivityViewHolder>(ActivityDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        val binding = ItemActivityBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ActivityViewHolder(binding, onDeleteClick)
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ActivityViewHolder(
        private val binding: ItemActivityBinding,
        private val onDeleteClick: (PhysicalActivity) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(activity: PhysicalActivity) {
            binding.tvActivityTitle.text = activity.activityType
            binding.tvActivityDescription.text = activity.specificActivity

            // Formatta data da yyyy-MM-dd a dd/MM/yyyy
            val formattedDate = formatDate(activity.date)
            binding.tvActivityDate.text = "Date: $formattedDate"

            // Formatta durata
            val hours = activity.durationMinutes / 60
            val minutes = activity.durationMinutes % 60

            val durationText = when {
                hours > 0 && minutes > 0 -> "$hours h $minutes min"
                hours > 0 -> "$hours h"
                else -> "$minutes min"
            }

            binding.tvActivityDuration.text = "Duration: $durationText"

            // Click su delete
            binding.btnDelete.setOnClickListener {
                onDeleteClick(activity)
            }
        }

        private fun formatDate(isoDate: String): String {
            return try {
                val parts = isoDate.split("-")
                if (parts.size == 3) {
                    "${parts[2]}/${parts[1]}/${parts[0]}"
                } else {
                    isoDate
                }
            } catch (e: Exception) {
                isoDate
            }
        }
    }

    class ActivityDiffCallback : DiffUtil.ItemCallback<PhysicalActivity>() {
        override fun areItemsTheSame(oldItem: PhysicalActivity, newItem: PhysicalActivity): Boolean {
            return oldItem.physicalActivityId == newItem.physicalActivityId
        }

        override fun areContentsTheSame(oldItem: PhysicalActivity, newItem: PhysicalActivity): Boolean {
            return oldItem == newItem
        }
    }
}