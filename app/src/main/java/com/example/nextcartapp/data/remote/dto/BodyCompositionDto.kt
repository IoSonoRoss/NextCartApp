package com.example.nextcartapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class BodyCompositionDto(
    @SerializedName("consumerId")
    val consumerId: Int,

    @SerializedName("measuredAt")
    val measuredAt: String,

    @SerializedName("weight")
    val weight: String,

    @SerializedName("height")
    val height: String
)

data class BodyCompositionsResponse(
    @SerializedName("value")
    val value: List<BodyCompositionDto>,

    @SerializedName("Count")
    val count: Int
)

data class CreateBodyCompositionDto(
    @SerializedName("measuredAt")  // ← CAMBIATO da "date" a "measuredAt"
    val measuredAt: String, // yyyy-MM-dd HH:mm:ss

    @SerializedName("weight")
    val weight: Double,

    @SerializedName("height")
    val height: Double
)