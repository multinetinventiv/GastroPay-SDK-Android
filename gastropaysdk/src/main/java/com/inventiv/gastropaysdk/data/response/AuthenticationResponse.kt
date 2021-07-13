package com.inventiv.gastropaysdk.data.response

data class AuthenticationResponse(
    var userToken: String?,
    var refreshToken: String?,
    var expirationTime: String?,
    var refreshTokenExpirationTime: String?
)