package com.example.nextcartapp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PantryItem(
    val id: Int,
    val product: Product,
    val quantity: Float,
    val lastUpdated: String
) : Parcelable