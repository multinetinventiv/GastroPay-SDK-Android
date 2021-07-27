package com.inventiv.gastropaysdk.data.request

import com.inventiv.gastropaysdk.common.BaseRequest

data class OtpConfirmRequest(
    val verificationCode: String,
    val smsCode: String
) : BaseRequest()