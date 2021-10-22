package com.inventiv.gastropaysdk.api

import com.inventiv.gastropaysdk.data.request.*
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
        @Query("cityId") cityId: String? = null,
        @Query("currentPage") pageIndex: Int
    ): MerchantListResponse

    @GET("merchant/merchant_detail/{merchantUid}")
    suspend fun merchantDetail(@Path("merchantUid") id: String): MerchantDetailResponse

    @POST("auth/login")
    suspend fun login(@Body login: LoginRequest): LoginResponse

    @POST("auth/otp_confirm")
    suspend fun otpConfirm(@Body login: OtpConfirmRequest): AuthenticationResponse

    @GET("auth/settings")
    suspend fun settings(): SettingsResponse

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

    @GET("merchant/search_criteria_tag_groups")
    suspend fun searchCriterias(
        @Query("cityId") cityId: String? = null,
        @Query("newFiltering") newFiltering: Boolean = true,
    ): List<TagGroupResponse>

    @GET("auth/term_and_condition/1")
    suspend fun getTermsAndCondition(): TermsAndConditionResponse

    @POST("notify/invoice_send")
    suspend fun invoiceSend(@Body request: InvoiceSendRequest): Response<Unit>

    @PUT("auth/update_notification_preferences/{id}")
    suspend fun updateNotificationPreferences(
        @Path("id") id: Int,
        @Body notificationPreferenceValue: NotificationPreferencesRequest
    ): Response<Unit>

    @POST("auth/notification_preferences")
    suspend fun notificationPreferences(): ArrayList<NotificationPreferencesResponse>
}