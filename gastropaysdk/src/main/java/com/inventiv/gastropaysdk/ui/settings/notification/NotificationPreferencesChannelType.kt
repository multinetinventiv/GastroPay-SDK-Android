package com.inventiv.gastropaysdk.ui.settings.notification

import com.google.gson.annotations.SerializedName
import com.inventiv.gastropaysdk.R

enum class NotificationPreferencesChannelType(var value: String, val resourceId : Int) {
    @SerializedName("1")
    SMS("1", R.string.settings_notification_channel_1_gastropay_sdk),

    @SerializedName("2")
    MAIL("2", R.string.settings_notification_channel_2_gastropay_sdk),

    @SerializedName("3")
    PUSH("3", -1)
}