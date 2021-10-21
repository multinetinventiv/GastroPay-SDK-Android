package com.inventiv.gastropaysdk.data.request

data class NotificationPreferencesRequest(
    val channel: Int,
    val newState: Int,
)