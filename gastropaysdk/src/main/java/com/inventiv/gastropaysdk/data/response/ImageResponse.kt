package com.inventiv.gastropaysdk.data.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
internal data class ImageResponse(
    val id: String?,
    val title: String?,
    val isShowcase: Boolean?,
    val orderId: Int?,
    val url: String?
) : Parcelable