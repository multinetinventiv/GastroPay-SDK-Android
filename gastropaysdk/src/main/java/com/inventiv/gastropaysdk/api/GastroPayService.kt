package com.inventiv.gastropaysdk.api

import com.inventiv.gastropaysdk.data.model.response.DummyResponse
import com.inventiv.gastropaysdk.data.model.response.MerchantPaging
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

internal interface GastroPayService {

    @GET("/posts/{id}")
    suspend fun getDummy(@Path("id") id: Int): DummyResponse

    @GET("merchant/merchants_info")
    suspend fun merchantsInfo(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("tags") tags: String?,
        @Query("isBonusPoint") isBonusPoint: Boolean? = false,
        @Query("merchantName") merchantName: String?,
        @Query("currentPage") pageIndex: Int
    ): MerchantPaging
}