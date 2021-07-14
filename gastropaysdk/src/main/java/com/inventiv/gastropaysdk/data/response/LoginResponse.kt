package com.inventiv.gastropaysdk.data.response

import com.inventiv.gastropaysdk.common.BaseResponse

data class LoginResponse(
    val endTime: String,
    val verificationCode: String
) : BaseResponse()