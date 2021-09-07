package com.inventiv.gastropaysdk.repository

import com.inventiv.gastropaysdk.data.Resource
import com.inventiv.gastropaysdk.data.response.CitiesResponse
import com.inventiv.gastropaysdk.data.response.MerchantDetailResponse
import com.inventiv.gastropaysdk.data.response.MerchantListResponse
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

    fun cities(): Flow<Resource<CitiesResponse>>
}