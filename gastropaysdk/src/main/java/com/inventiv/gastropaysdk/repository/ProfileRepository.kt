package com.inventiv.gastropaysdk.repository

import com.inventiv.gastropaysdk.data.Resource
import com.inventiv.gastropaysdk.data.request.NotificationPreferencesRequest
import com.inventiv.gastropaysdk.ui.settings.notification.NotificationPreferencesBase
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

internal interface ProfileRepository {
    fun notificationPreferences(): Flow<Resource<List<NotificationPreferencesBase>>>
    fun updateNotificationPreferences(
        id: Int,
        request: NotificationPreferencesRequest
    ): Flow<Resource<Response<Unit>>>
}