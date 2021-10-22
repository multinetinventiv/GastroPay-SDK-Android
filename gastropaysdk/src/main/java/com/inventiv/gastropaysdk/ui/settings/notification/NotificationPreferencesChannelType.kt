package com.inventiv.gastropaysdk.ui.settings.notification

import com.google.gson.annotations.SerializedName

enum class NotificationPreferencesChannelType(var value: String) {
    @SerializedName("1")
    SMS("1"),

    @SerializedName("2")
    MAIL("2"),

    @SerializedName("3")
    PUSH("3")
}