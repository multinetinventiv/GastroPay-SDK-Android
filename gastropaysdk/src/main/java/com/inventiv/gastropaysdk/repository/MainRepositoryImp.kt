package com.inventiv.gastropaysdk.repository

import com.inventiv.gastropaysdk.api.GastroPayService
import com.inventiv.gastropaysdk.common.BaseRepository
import com.inventiv.gastropaysdk.data.model.Resource
import com.inventiv.gastropaysdk.data.model.response.DummyResponse
import com.inventiv.gastropaysdk.data.model.safeFlow
import kotlinx.coroutines.flow.Flow

internal class MainRepositoryImp(private val gastroPayService: GastroPayService) : MainRepository,
    BaseRepository() {

    override fun getDummy(id: Int): Flow<Resource<DummyResponse>> {
        return safeFlow { gastroPayService.getDummy(id) }
    }

}