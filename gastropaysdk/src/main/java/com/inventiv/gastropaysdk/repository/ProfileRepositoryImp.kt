package com.inventiv.gastropaysdk.repository

import com.inventiv.gastropaysdk.api.GastroPayService
import com.inventiv.gastropaysdk.common.BaseRepository
import com.inventiv.gastropaysdk.data.Resource
import com.inventiv.gastropaysdk.data.request.NotificationPreferencesRequest
import com.inventiv.gastropaysdk.data.response.NotificationPreferencesResponse
import com.inventiv.gastropaysdk.data.safeFlow
import com.inventiv.gastropaysdk.data.transformFlow
import com.inventiv.gastropaysdk.ui.settings.notification.NotificationPreferencesBase
import com.inventiv.gastropaysdk.ui.settings.notification.NotificationPreferencesChannelType
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

internal class ProfileRepositoryImp(private val gastroPayService: GastroPayService) :
    ProfileRepository, BaseRepository() {

    override fun notificationPreferences(): Flow<Resource<List<NotificationPreferencesBase>>> {
        return transformFlow(
            suspendFun = {
                gastroPayService.notificationPreferences()
            },
            transformFun = { notificationPreferenceList ->
                mapNotificationPreferenceToNotificationPreferenceBase(notificationPreferenceList)
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

    private fun mapNotificationPreferenceToNotificationPreferenceBase(
        networkNotificationPreferenceList: List<NotificationPreferencesResponse>
    ): List<NotificationPreferencesBase> {
        if (networkNotificationPreferenceList.isNullOrEmpty()) {
            return mutableListOf()
        }

        val notificationList: MutableList<NotificationPreferencesBase> = mutableListOf()

        val groupedList = networkNotificationPreferenceList.groupBy { it.id }
        for ((groupId, groupItemList) in groupedList) {
            notificationList.add(
                NotificationPreferencesBase.NotificationPreferencesHeader(
                    groupId,
                    groupItemList.first().label
                )
            )
            groupItemList.sortedBy { it.channel }
                .filter { it.channel != NotificationPreferencesChannelType.PUSH }
                .forEach {
                    notificationList.add(
                        NotificationPreferencesBase.NotificationPreferencesItem(
                            it.id,
                            it.channel,
                            it.state
                        )
                    )
                }
        }

        return notificationList
    }
}