package com.inventiv.gastropaysdk.data.request

data class OtpConfirmRequest(
    val verificationCode: String,
    val smsCode: String
)