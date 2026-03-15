package com.example.nextcartapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ProductDto(
    @SerializedName("productId")
    val productId: String,

    @SerializedName("name")
    val name: String?,

    @SerializedName("itName")
    val itName: String?,

    @SerializedName("productCategory")
    val productCategory: ProductCategoryDto?,

    @SerializedName("productDiets")
    val productDiets: List<ProductDietDto>? = emptyList(),

    @SerializedName("productClaims")
    val productClaims: List<ProductClaimDto>? = emptyList(),

    @SerializedName("productAllergens")
    val productAllergens: List<ProductAllergenDto>? = emptyList(),

    @SerializedName("nutritionalInformationValues")
    val nutritionalInformationValues: List<NutritionalInfoDto>? = emptyList()
)

// ProductCategory DTO
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

data class ProductDietDto(
    @SerializedName("diet")
    val diet: DietInfoDto?
)

data class DietInfoDto(
    @SerializedName("description")
    val description: String?
)

data class ProductClaimDto(
    @SerializedName("claim")
    val claim: ClaimInfoDto?
)

data class ClaimInfoDto(
    @SerializedName("description")
    val description: String?
)

data class ProductAllergenDto(
    @SerializedName("allergen")
    val allergen: AllergenInfoDto?
)

data class AllergenInfoDto(
    @SerializedName("description")
    val description: String?
)

data class NutritionalInfoDto(
    @SerializedName("nutritionalInformation")
    val nutritionalInformation: NutrientInfoDto?,

    @SerializedName("value")
    val value: String?
)

data class NutrientInfoDto(
    @SerializedName("name")
    val name: String?,

    @SerializedName("unit")
    val unit: String?
)