package com.inventiv.gastropaysdk.repository

import com.inventiv.gastropaysdk.api.GastroPayService
import com.inventiv.gastropaysdk.common.BaseRepository
import com.inventiv.gastropaysdk.data.Resource
import com.inventiv.gastropaysdk.data.response.SettingsResponse
import com.inventiv.gastropaysdk.data.safeFlow
import kotlinx.coroutines.flow.Flow

internal class MainRepositoryImp(private val gastroPayService: GastroPayService) : MainRepository,
    BaseRepository() {
    override fun getSettings(): Flow<Resource<SettingsResponse>> {
        return safeFlow(
            suspendFun = {
                gastroPayService.settings()
            }
        )
    }
}