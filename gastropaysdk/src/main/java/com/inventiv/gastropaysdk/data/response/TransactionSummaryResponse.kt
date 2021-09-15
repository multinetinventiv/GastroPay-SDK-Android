package com.inventiv.gastropaysdk.data.response

import com.inventiv.gastropaysdk.common.BaseResponse

internal data class TransactionSummaryResponse(
    val totalCashback: AmountModel
) : BaseResponse()