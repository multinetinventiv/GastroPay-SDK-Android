package com.inventiv.gastropaysdk.ui.settings.notification

import com.google.gson.annotations.SerializedName
import com.inventiv.gastropaysdk.R

enum class NotificationPreferencesChannelType(var value: String, var resourceId: Int) {
    @SerializedName("1")
    SMS("1", R.string.settings_notification_channel_1),

    @SerializedName("2")
    MAIL("2", R.string.settings_notification_channel_2),

}