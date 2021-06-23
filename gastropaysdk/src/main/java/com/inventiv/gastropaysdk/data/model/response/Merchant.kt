package com.inventiv.gastropaysdk.data.model.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class Merchant(
    @field:SerializedName("merchantId")
    val merchantId: String,
    @field:SerializedName("name")
    val name: String,
    @field:SerializedName("latitude")
    val latitude: Double,
    @field:SerializedName("longitude")
    val longitude: Double,
    @field:SerializedName("distance")
    var distance: Int,
    @field:SerializedName("rewardPercentage")
    val rewardPercentage: String?,
    @field:SerializedName("logoUrl")
    val logoUrl: String?,
    @field:SerializedName("images")
    val images: List<Image>?,
    @field:SerializedName("showcaseImageUrl")
    val showcaseImageUrl: String?,
    @field:SerializedName("isBonusPoint")
    val isBonusPoint: Boolean?
) : Parcelable