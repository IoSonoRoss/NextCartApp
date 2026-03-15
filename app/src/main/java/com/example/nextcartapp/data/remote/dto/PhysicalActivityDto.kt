package com.example.nextcartapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class PhysicalActivityDto(
    @SerializedName("physicalActivityId")
    val physicalActivityId: Int,

    @SerializedName("specificActivity")
    val specificActivity: String,

    @SerializedName("durationMinutes")
    val durationMinutes: Int,

    @SerializedName("date")
    val date: String,

    @SerializedName("activity")
    val activity: ActivityDto
)

data class ActivityDto(
    @SerializedName("activityId")
    val activityId: Int,

    @SerializedName("ActivityType")
    val activityType: String,

    @SerializedName("SpecificActivity")
    val specificActivity: String,

    @SerializedName("MET")
    val met: String
)

data class CreatePhysicalActivityDto(
    @SerializedName("specificActivity")
    val specificActivity: String,

    @SerializedName("date")
    val date: String,

    @SerializedName("durationMinutes")
    val durationMinutes: Int,

    @SerializedName("activity")
    val activityId: Int
)