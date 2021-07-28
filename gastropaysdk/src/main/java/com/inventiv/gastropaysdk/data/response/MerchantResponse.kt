package com.inventiv.gastropaysdk.data.response

internal data class MerchantResponse(
    val merchantId: String,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    var distance: Int,
    val rewardPercentage: String?,
    val logoUrl: String?,
    val images: List<ImageResponse>?,
    val showcaseImageUrl: String?,
    val isBonusPoint: Boolean?
)