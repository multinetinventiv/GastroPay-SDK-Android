package com.inventiv.gastropaysdk.data.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.inventiv.gastropaysdk.common.BaseResponse

data class MerchantPaging(
    @field:SerializedName("merchants")
    val merchants: List<Merchant>,
    @field:SerializedName("isLastPage")
    val isLastPage: Boolean,
    @Expose(serialize = false, deserialize = false)
    var pageIndex: Int = 0
) : BaseResponse()