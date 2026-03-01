package com.example.nextcartapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class DietDto(
    @SerializedName("dietId")
    val dietId: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("restrictionLevel")
    val restrictionLevel: String?
)