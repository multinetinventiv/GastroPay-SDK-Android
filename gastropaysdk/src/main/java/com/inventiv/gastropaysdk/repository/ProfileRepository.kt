package com.inventiv.gastropaysdk.repository

import com.inventiv.gastropaysdk.data.Resource
import com.inventiv.gastropaysdk.data.request.ConfirmProvisionRequest
import com.inventiv.gastropaysdk.data.request.NotificationPreferencesRequest
import com.inventiv.gastropaysdk.data.response.NotificationPreferencesResponse
import com.inventiv.gastropaysdk.data.response.TermsAndConditionResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

internal interface ProfileRepository {
    fun notificationPreferences(): Flow<Resource<List<NotificationPreferencesResponse>>>
    fun updateNotificationPreferences(request: NotificationPreferencesRequest): Flow<Resource<Response<Unit>>>

}