package com.inventiv.gastropaysdk.api

import com.inventiv.gastropaysdk.data.model.response.MerchantDetail
import com.inventiv.gastropaysdk.data.model.response.MerchantPaging
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

internal interface GastroPayService {

    @GET("merchant/merchants_info")
    suspend fun merchantsInfo(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("tags") tags: String?,
        @Query("isBonusPoint") isBonusPoint: Boolean? = false,
        @Query("merchantName") merchantName: String?,
        @Query("currentPage") pageIndex: Int
    ): MerchantPaging

    @GET("merchant/merchant_detail/{merchantUid}")
    suspend fun merchantDetail(@Path("merchantUid") id: String): MerchantDetail
}