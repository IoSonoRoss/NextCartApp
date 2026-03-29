package com.example.nextcartapp.presentation.ui.meal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.nextcartapp.R
import com.example.nextcartapp.domain.model.Meal

class MealAdapter(
    private val onDeleteClick: (Meal) -> Unit
) : ListAdapter<Meal, MealAdapter.ViewHolder>(MealDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_meal, parent, false)
        return ViewHolder(view, onDeleteClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(view: View, val onDelete: (Meal) -> Unit) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvMealName)
        val tvType: TextView = view.findViewById(R.id.tvMealType)
        val tvDate: TextView = view.findViewById(R.id.tvMealDate)
        val tvKcal: TextView = view.findViewById(R.id.tvMealKcal)
        val btnDelete: View = view.findViewById(R.id.btnDelete)

        fun bind(meal: Meal) {
            tvName.text = meal.name
            tvType.text = meal.type
            tvDate.text = "Data: ${meal.date}"
            tvKcal.text = meal.kcal?.let { "Calorie: $it kcal" } ?: "Calorie: N/D"
            btnDelete.setOnClickListener { onDelete(meal) }
        }
    }

    class MealDiffCallback : DiffUtil.ItemCallback<Meal>() {
        override fun areItemsTheSame(oldItem: Meal, newItem: Meal) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Meal, newItem: Meal) = oldItem == newItem
    }
}