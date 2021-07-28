package com.inventiv.gastropaysdk.data.request

import com.inventiv.gastropaysdk.common.BaseRequest

internal data class LoginRequest(
    val gsmNumber: String,
    val mobileDeviceToken: String
) : BaseRequest()