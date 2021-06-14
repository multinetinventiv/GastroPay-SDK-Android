package com.inventiv.gastropaysdk.repository

import com.inventiv.gastropaysdk.api.GastroPayService
import com.inventiv.gastropaysdk.common.BaseRepository
import com.inventiv.gastropaysdk.data.model.Resource
import com.inventiv.gastropaysdk.data.model.response.MerchantPaging
import com.inventiv.gastropaysdk.data.model.safeFlow
import kotlinx.coroutines.flow.Flow

internal class MerchantRepositoryImp(private val gastroPayService: GastroPayService) :
    MerchantRepository, BaseRepository() {

    override fun getMerchants(
        latitude: Double,
        longitude: Double,
        tags: String?,
        merchantName: String?,
        page: Int
    ): Flow<Resource<MerchantPaging>> {

        return safeFlow(suspendFun = {
            gastroPayService.merchantsInfo(
                latitude,
                longitude,
                tags,
                false,
                merchantName,
                page
            )
        }, modifyFun = { resource ->
            if (resource is Resource.Success) {
                resource.data.pageIndex = page
            }
            return@safeFlow resource
        })
    }
}