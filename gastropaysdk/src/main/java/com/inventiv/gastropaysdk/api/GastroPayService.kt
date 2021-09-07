package com.inventiv.gastropaysdk.api

import com.inventiv.gastropaysdk.data.request.ConfirmProvisionRequest
import com.inventiv.gastropaysdk.data.request.LoginRequest
import com.inventiv.gastropaysdk.data.request.OtpConfirmRequest
import com.inventiv.gastropaysdk.data.request.ProvisionInformationRequest
import com.inventiv.gastropaysdk.data.response.*
import retrofit2.Response
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
    ): MerchantListResponse

    @GET("merchant/merchant_detail/{merchantUid}")
    suspend fun merchantDetail(@Path("merchantUid") id: String): MerchantDetailResponse

    @POST("auth/login")
    suspend fun login(@Body login: LoginRequest): LoginResponse

    @POST("auth/otp_confirm")
    suspend fun otpConfirm(@Body login: OtpConfirmRequest): AuthenticationResponse

    @GET("wallet/transactions/{id}/{end_time}")
    suspend fun getLastTransactions(
        @Path("id") id: String,
        @Path("end_time") endTime: String
    ): List<LastTransactionsResponse>

    @GET("wallet/wallets")
    suspend fun getWallet(): WalletResponse

    @GET("wallet/transaction_summary")
    suspend fun getTransactionSummary(): TransactionSummaryResponse

    @POST("pos/provision_information")
    suspend fun provisionInformation(@Body request: ProvisionInformationRequest): ProvisionInformationResponse

    @POST("pos/confirm_provision")
    suspend fun confirmProvision(@Body request: ConfirmProvisionRequest): Response<Unit>

    @GET("wallet/cards")
    suspend fun getBankCards(): List<BankCardResponse>

    @GET("merchant/cities")
    suspend fun cities(): CitiesResponse

}