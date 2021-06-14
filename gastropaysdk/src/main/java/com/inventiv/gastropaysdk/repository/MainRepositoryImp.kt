package com.inventiv.gastropaysdk.repository

import com.inventiv.gastropaysdk.api.GastroPayService
import com.inventiv.gastropaysdk.common.BaseRepository
import com.inventiv.gastropaysdk.data.model.Resource
import com.inventiv.gastropaysdk.data.model.response.DummyResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal class MainRepositoryImp(private val gastroPayService: GastroPayService) : MainRepository,
    BaseRepository() {

    override fun getDummy(id: Int): Flow<Resource<DummyResponse>> {
        return flow {}
        /*return safeFlow({
            gastroPayService.getDummy(id)
        },null)*/
    }

}