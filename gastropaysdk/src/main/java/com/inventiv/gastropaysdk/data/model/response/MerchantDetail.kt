package com.inventiv.gastropaysdk.data.model.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlin.math.roundToInt

@Parcelize
data class MerchantDetail(
    @field:SerializedName("merchantId")
    val merchantId: String,
    @field:SerializedName("tags")
    val tags: List<Tag>,
    @field:SerializedName("pageContent")
    val pageContent: PageContent?,
    @field:SerializedName("address")
    val address: Address?,
    @field:SerializedName("name")
    val name: String,
    @field:SerializedName("logoUrl")
    val logoUrl: String?,
    @field:SerializedName("images")
    val images: List<Image>,
    @field:SerializedName("isBonusPoint")
    val isBonusPoint: Boolean,
    @field:SerializedName("rewardPercentage")
    val rewardPercentage: String?,
    @field:SerializedName("distance")
    val distance: String?,
    @field:SerializedName("latitude")
    val latitude: Double,
    @field:SerializedName("longitude")
    val longitude: Double,
    @field:SerializedName("note")
    val note: String?,
    @field:SerializedName("rate")
    val rate: Float,
    @field:SerializedName("showcaseImageUrl")
    val showcaseImageUrl: String?,
    @field:SerializedName("phoneNumber")
    var phoneNumber: String?,
    @field:SerializedName("gsmNumber")
    var gsmNumber: String?
) : Parcelable {

    fun rate(): Int = rate.roundToInt()

    fun isPageContentAvailable(): Boolean {
        return (pageContent != null && !pageContent.id.isNullOrEmpty())
    }
}

@Parcelize
data class PageContent(
    @field:SerializedName("id")
    val id: String?,
    @field:SerializedName("title")
    val title: String?,
    @field:SerializedName("content")
    val content: String?,
    @field:SerializedName("icon")
    val icon: Image?
) : Parcelable

@Parcelize
data class Tag(
    @field:SerializedName("id")
    val id: String,
    @field:SerializedName("tagName")
    val tagName: String,
    @field:SerializedName("icon")
    val icon: Image,
    var isSelected: Boolean = false
) : Parcelable

@Parcelize
data class Address(
    @field:SerializedName("id")
    val id: String,
    @field:SerializedName("city")
    val city: String?,
    @field:SerializedName("district")
    val district: String?,
    @field:SerializedName("neighbourhood")
    val neighbourhood: String?,
    @field:SerializedName("fullAddressText")
    val fullAddress: String?
) : Parcelable