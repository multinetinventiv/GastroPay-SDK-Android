package com.inventiv.gastropaysdk.ui.settings.notification

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class NotificationPreferencesBases {
    @Parcelize
    data class NotificationPreferencesHeader(
        val id: Int?,
        var label: String?
    ) : NotificationPreferencesBases(), Parcelable

    @Parcelize
    data class NotificationPreferencesItem(
        val id: Int?,
        var preferencesChannel: NotificationPreferencesChannelType?,
        var preferencesState: NotificationPreferencesStateType?
    ) : NotificationPreferencesBases(), Parcelable
}

