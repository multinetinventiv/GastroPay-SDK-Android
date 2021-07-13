package com.inventiv.gastropaysdk.repository

import com.inventiv.gastropaysdk.data.response.MerchantDetailResponse
import com.inventiv.gastropaysdk.data.response.MerchantListResponse
import com.inventiv.gastropaysdk.model.Resource
import kotlinx.coroutines.flow.Flow

internal interface MerchantRepository {
    fun getMerchants(
        latitude: Double,
        longitude: Double,
        tags: String? = null,
        merchantName: String? = null,
        page: Int
    ): Flow<Resource<MerchantListResponse>>

    fun getMerchantDetail(
        id: String
    ): Flow<Resource<MerchantDetailResponse>>
}