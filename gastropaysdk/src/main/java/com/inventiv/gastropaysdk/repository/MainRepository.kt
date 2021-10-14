package com.inventiv.gastropaysdk.repository

import com.inventiv.gastropaysdk.data.Resource
import com.inventiv.gastropaysdk.data.response.SettingsResponse
import kotlinx.coroutines.flow.Flow

internal interface MainRepository {
    fun getSettings(): Flow<Resource<SettingsResponse>>
}