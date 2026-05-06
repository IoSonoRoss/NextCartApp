package com.example.nextcartapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ProductDto(
    @SerializedName("productId") // Cambiato da productId a product_id (come da query SQL)
    val productId: String,

    @SerializedName("name")
    val name: String?,

    @SerializedName("it_name") // Cambiato da itName a it_name
    val itName: String?,

    @SerializedName("product_category") // Cambiato per coerenza con il DB
    val productCategory: ProductCategoryDto?,

    @SerializedName("image_url")
    val imageUrl: String?,

    @SerializedName("productDiets")
    val productDiets: List<ProductDietDto>? = emptyList(),

    @SerializedName("productClaims")
    val productClaims: List<ProductClaimDto>? = emptyList(),

    @SerializedName("productAllergens")
    val productAllergens: List<ProductAllergenDto>? = emptyList(),

    @SerializedName("nutritionalInformationValues")
    val nutritionalInformationValues: List<NutritionalInfoDto>? = emptyList(),

    @SerializedName("unit_type") // Cambiato da unitType a unit_type
    val unitType: String?, // Nullable per sicurezza

    @SerializedName("default_package_size") // Cambiato da defaultPackageSize a default_package_size
    val defaultPackageSize: Float?
)

data class ProductCategoryDto(
    @SerializedName("product_category_id") // Allineato al DB
    val productCategoryId: String,

    @SerializedName("group")
    val group: String?,

    @SerializedName("category")
    val category: String,

    @SerializedName("standard_portion") // Allineato al DB
    val standardPortion: String?
)

data class ProductDietDto(
    @SerializedName("diet")
    val diet: DietInfoDto?
)

data class DietInfoDto(
    @SerializedName("diet_id") // Dalla query SQL risulta 'diet_id'
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
    @SerializedName("allergen_name") // Dalla query SQL risulta 'allergen_name'
    val description: String?
)

data class NutritionalInfoDto(
    @SerializedName("nutrient") // Deve essere "nutrient" come nel Service sopra
    val nutritionalInformation: NutrientInfoDto?,
    @SerializedName("value")
    val value: String?
)

data class NutrientInfoDto(
    @SerializedName("nutrient_it") // Dalla query SQL risulta 'nutrient_it'
    val name: String?,

    @SerializedName("unitOfMeasure") // Dalla query SQL risulta 'unitOfMeasure'
    val unit: String?
)