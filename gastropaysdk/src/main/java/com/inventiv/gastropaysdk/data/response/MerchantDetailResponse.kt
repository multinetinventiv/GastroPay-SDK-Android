package com.inventiv.gastropaysdk.data.response

import kotlin.math.roundToInt

data class MerchantDetailResponse(
    val merchantId: String,
    val tags: List<Tag>,
    val pageContent: PageContent?,
    val address: Address?,
    val name: String,
    val logoUrl: String?,
    val images: List<ImageResponse>,
    val isBonusPoint: Boolean,
    val rewardPercentage: String?,
    val distance: String?,
    val latitude: Double,
    val longitude: Double,
    val note: String?,
    val rate: Float,
    val showcaseImageUrl: String?,
    var phoneNumber: String?,
    var gsmNumber: String?
) {

    fun rate(): Int = rate.roundToInt()

    fun isPageContentAvailable(): Boolean {
        return (pageContent != null && !pageContent.id.isNullOrEmpty())
    }
}

data class PageContent(
    val id: String?,
    val title: String?,
    val content: String?,
    val icon: ImageResponse?
)

data class Tag(
    val id: String,
    val tagName: String,
    val icon: ImageResponse,
    var isSelected: Boolean = false
)

data class Address(
    val id: String,
    val city: String?,
    val district: String?,
    val neighbourhood: String?,
    val fullAddress: String?
)