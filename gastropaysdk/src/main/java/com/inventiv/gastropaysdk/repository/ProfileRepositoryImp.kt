package com.inventiv.gastropaysdk.repository

import com.inventiv.gastropaysdk.api.GastroPayService
import com.inventiv.gastropaysdk.common.BaseRepository
import com.inventiv.gastropaysdk.data.Resource
import com.inventiv.gastropaysdk.data.request.NotificationPreferencesRequest
import com.inventiv.gastropaysdk.data.response.NotificationPreferencesResponse
import com.inventiv.gastropaysdk.data.safeFlow
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

internal class ProfileRepositoryImp(private val gastroPayService: GastroPayService) :
    ProfileRepository, BaseRepository() {

    override fun notificationPreferences(): Flow<Resource<ArrayList<NotificationPreferencesResponse>>> {
        return safeFlow(
            suspendFun = {
                gastroPayService.notificationPreferences()
            }
        )
    }

    override fun updateNotificationPreferences(
        id: Int,
        request: NotificationPreferencesRequest
    ): Flow<Resource<Response<Unit>>> {
        return safeFlow(
            suspendFun = {
                gastroPayService.updateNotificationPreferences(id, request)
            }
        )
    }
}