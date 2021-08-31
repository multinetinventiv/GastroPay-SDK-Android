package com.inventiv.gastropaysdk.data.response


internal data class ProvisionInformationResponse(
    val merchantId: String?,
    val token: String?,
    val amount: AmountModel?,
    val merchantName: String?,
    val imageUrl: String?,
    val totalAmount: AmountModel?,
    val availableAmount: AmountModel?,
    val usingAvailableAmount: AmountModel?,
    val merchantUid: String?,
    val callType: Int?,
    val terminalUid: String?,
)