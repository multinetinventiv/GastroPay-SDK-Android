package com.inventiv.gastropaysdk.ui.settings.notification

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class NotificationPreferencesBase {
    @Parcelize
    data class NotificationPreferencesHeader(
        val id: Int,
        var label: String
    ) : NotificationPreferencesBase(), Parcelable

    @Parcelize
    data class NotificationPreferencesItem(
        val id: Int,
        var preferencesChannel: NotificationPreferencesChannelType,
        var preferencesState: NotificationPreferencesStateType
    ) : NotificationPreferencesBase(), Parcelable
}

