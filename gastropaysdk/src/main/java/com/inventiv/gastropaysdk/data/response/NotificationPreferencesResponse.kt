package com.inventiv.gastropaysdk.data.response

import com.inventiv.gastropaysdk.ui.settings.notification.NotificationPreferencesChannelType
import com.inventiv.gastropaysdk.ui.settings.notification.NotificationPreferencesStateType

internal data class NotificationPreferencesResponse(
    var id: Int?,
    var label: String?,
    var channel: NotificationPreferencesChannelType?,
    var state: NotificationPreferencesStateType?
)