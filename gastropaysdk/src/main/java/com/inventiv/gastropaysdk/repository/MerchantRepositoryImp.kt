package com.inventiv.gastropaysdk.repository

import com.inventiv.gastropaysdk.api.GastroPayService
import com.inventiv.gastropaysdk.common.BaseRepository
import com.inventiv.gastropaysdk.data.Resource
import com.inventiv.gastropaysdk.data.response.CitiesResponse
import com.inventiv.gastropaysdk.data.response.MerchantDetailResponse
import com.inventiv.gastropaysdk.data.response.MerchantListResponse
import com.inventiv.gastropaysdk.data.response.TagGroupResponse
import com.inventiv.gastropaysdk.data.safeFlow
import kotlinx.coroutines.flow.Flow

internal class MerchantRepositoryImp(private val gastroPayService: GastroPayService) :
    MerchantRepository, BaseRepository() {

    override fun getMerchants(
        latitude: Double,
        longitude: Double,
        tags: String?,
        merchantName: String?,
        cityId: String?,
        page: Int
    ): Flow<Resource<MerchantListResponse>> {

        return safeFlow(
            suspendFun = {
                gastroPayService.merchantsInfo(
                    latitude,
                    longitude,
                    tags,
                    false,
                    merchantName,
                    cityId,
                    page
                )
            }
        )
    }

    override fun getMerchantDetail(
        id: String
    ): Flow<Resource<MerchantDetailResponse>> {
        return safeFlow {
            gastroPayService.merchantDetail(id)
        }
    }

    override fun getCities(): Flow<Resource<CitiesResponse>> {
        return safeFlow {
            gastroPayService.cities()
        }
    }

    override fun getSearchCriteria(cityId: String?): Flow<Resource<List<TagGroupResponse>>> {
        return safeFlow {
            gastroPayService.searchCriterias(cityId)
        }
    }
}