package com.inventiv.gastropaysdk.repository

import com.inventiv.gastropaysdk.data.model.Resource
import com.inventiv.gastropaysdk.data.model.response.MerchantPaging
import kotlinx.coroutines.flow.Flow

internal interface MerchantRepository {
    fun getMerchants(
        latitude: Double,
        longitude: Double,
        tags: String? = null,
        merchantName: String? = null,
        page: Int
    ): Flow<Resource<MerchantPaging>>
}