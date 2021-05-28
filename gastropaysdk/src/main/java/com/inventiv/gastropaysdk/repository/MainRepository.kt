package com.inventiv.gastropaysdk.repository

import com.inventiv.gastropaysdk.api.ApiService
import com.inventiv.gastropaysdk.api.response.DummyResponse
import com.inventiv.gastropaysdk.data.model.Resource
import com.inventiv.gastropaysdk.data.model.safeFlow
import kotlinx.coroutines.flow.Flow

internal class MainRepository(private val apiService: ApiService) {

    fun getDummy(id: Int): Flow<Resource<DummyResponse>> {
        return safeFlow { apiService.getDummy(id) }
    }

}