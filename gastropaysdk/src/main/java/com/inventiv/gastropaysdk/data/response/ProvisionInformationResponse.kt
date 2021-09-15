package com.inventiv.gastropaysdk.data.response

import android.os.Parcelable
import com.inventiv.gastropaysdk.common.BaseResponse
import kotlinx.parcelize.Parcelize

@Parcelize
internal data class ProvisionInformationResponse(
    val merchantId: String,
    val token: String,
    val amount: AmountModel,
    val merchantName: String?,
    val imageUrl: String?,
    val totalAmount: AmountModel,
    val availableAmount: AmountModel,
    val usingAvailableAmount: AmountModel,
    val merchantUid: String,
    val callType: Int?,
    val terminalUid: String?,
) : Parcelable, BaseResponse()