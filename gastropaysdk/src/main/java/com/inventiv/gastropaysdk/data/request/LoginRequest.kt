package com.inventiv.gastropaysdk.data.request

data class LoginRequest(
    val gsmNumber: String,
    val mobileDeviceToken: String
)