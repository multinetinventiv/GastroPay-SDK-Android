package com.inventiv.gastropaysdk.data.response

import com.inventiv.gastropaysdk.common.BaseResponse

data class AuthenticationResponse(
    var userToken: String?,
    var refreshToken: String?,
    var expirationTime: String?,
    var refreshTokenExpirationTime: String?
) : BaseResponse()