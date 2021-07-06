package com.inventiv.gastropaysdk.data.response

import com.inventiv.gastropaysdk.common.BaseResponse

data class MerchantsResponse(
    val merchants: List<MerchantResponse>,
    val isLastPage: Boolean
) : BaseResponse()