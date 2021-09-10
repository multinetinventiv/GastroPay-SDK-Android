package com.inventiv.gastropaysdk.data.response

import com.inventiv.gastropaysdk.common.BaseResponse

internal data class WalletResponse(
    val walletUId: String,
) : BaseResponse()