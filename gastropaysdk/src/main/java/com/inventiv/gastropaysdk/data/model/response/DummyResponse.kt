package com.inventiv.gastropaysdk.data.model.response

import com.google.gson.annotations.SerializedName

internal data class DummyResponse(
    @SerializedName("userId") val userId: Int,
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("body") val body: String
)