package com.example.nextcartapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ProductDto(
    @SerializedName("productId")
    val productId: String,

    @SerializedName("name")
    val name: String?,

    @SerializedName("it_name")
    val itName: String?,

    @SerializedName("productCategory")
    val productCategory: ProductCategoryDto?
)

data class ProductCategoryDto(
    @SerializedName("productCategoryId")
    val productCategoryId: String,

    @SerializedName("group")
    val group: String?,

    @SerializedName("category")
    val category: String,

    @SerializedName("standardPortion")
    val standardPortion: String?
)