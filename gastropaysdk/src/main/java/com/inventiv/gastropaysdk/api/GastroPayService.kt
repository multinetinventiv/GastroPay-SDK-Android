package com.inventiv.gastropaysdk.api

import com.inventiv.gastropaysdk.data.request.LoginRequest
import com.inventiv.gastropaysdk.data.response.LoginResponse
import com.inventiv.gastropaysdk.data.response.MerchantDetailResponse
import com.inventiv.gastropaysdk.data.response.MerchantsResponse
import retrofit2.http.*

internal interface GastroPayService {

    @GET("merchant/merchants_info")
    suspend fun merchantsInfo(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("tags") tags: String?,
        @Query("isBonusPoint") isBonusPoint: Boolean? = false,
        @Query("merchantName") merchantName: String?,
        @Query("currentPage") pageIndex: Int
    ): MerchantsResponse

    @GET("merchant/merchant_detail/{merchantUid}")
    suspend fun merchantDetail(@Path("merchantUid") id: String): MerchantDetailResponse

    @POST("auth/login")
    suspend fun login(@Body login: LoginRequest): LoginResponse
}