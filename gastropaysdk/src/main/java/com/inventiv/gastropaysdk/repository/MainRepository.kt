package com.inventiv.gastropaysdk.repository

import com.inventiv.gastropaysdk.data.model.Resource
import com.inventiv.gastropaysdk.data.model.response.DummyResponse
import kotlinx.coroutines.flow.Flow

internal interface MainRepository {
    fun getDummy(id: Int): Flow<Resource<DummyResponse>>
}