package com.inventiv.gastropaysdk.data.response

import com.inventiv.gastropaysdk.common.BaseResponse

internal data class MerchantListResponse(
    val merchants: List<MerchantResponse>,
    val isLastPage: Boolean
) : BaseResponse()