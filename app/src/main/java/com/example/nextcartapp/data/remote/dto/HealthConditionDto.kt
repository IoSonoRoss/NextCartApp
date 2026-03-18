package com.example.nextcartapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class HealthConditionDto(
    @SerializedName("healthConditionId")
    val healthConditionId: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("category")
    val category: HealthCategoryDto? = null
)

data class HealthCategoryDto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("code")
    val code: String,

    @SerializedName("label")
    val label: String
)

data class HealthCategoriesResponse(
    @SerializedName("value")
    val value: List<HealthCategoryDto>,

    @SerializedName("Count")
    val count: Int
)

data class HealthConditionsResponse(
    @SerializedName("value")
    val value: List<HealthConditionDto>,

    @SerializedName("Count")
    val count: Int
)

data class FilterHealthConditionsDto(
    @SerializedName("categoryCode")
    val categoryCode: String
)

data class UpdateHealthConditionsDto(
    @SerializedName("healthConditionIds")
    val healthConditionIds: List<String>
)