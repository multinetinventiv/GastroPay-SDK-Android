package com.inventiv.gastropaysdk.data.model.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Image(
    @field:SerializedName("id")
    val id: String?,
    @field:SerializedName("title")
    val title: String?,
    @field:SerializedName("isShowcase")
    val isShowcase: Boolean?,
    @field:SerializedName("orderId")
    val orderId: Int?,
    @field:SerializedName("url")
    val url: String?
) : Parcelable