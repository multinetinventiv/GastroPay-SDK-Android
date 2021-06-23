package com.inventiv.gastropaysdk.data.model.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlin.math.roundToInt

@Parcelize
data class MerchantDetail(
    val merchantId: String,
    val tags: List<Tag>,
    val pageContent: PageContent?,
    val address: Address?,
    val name: String,
    val logoUrl: String?,
    val images: List<Image>,
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
) : Parcelable {

    fun rate(): Int = rate.roundToInt()

    fun isPageContentAvailable(): Boolean {
        return (pageContent != null && !pageContent.id.isNullOrEmpty())
    }
}

@Parcelize
data class PageContent(
    val id: String?,
    val title: String?,
    val content: String?,
    val icon: Image?
) : Parcelable

@Parcelize
data class Tag(
    val id: String,
    val tagName: String,
    val icon: Image,
    var isSelected: Boolean = false
) : Parcelable

@Parcelize
data class Address(
    val id: String,
    val city: String?,
    val district: String?,
    val neighbourhood: String?,
    val fullAddress: String?
) : Parcelable