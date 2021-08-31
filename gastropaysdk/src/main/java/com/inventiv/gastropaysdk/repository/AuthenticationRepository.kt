package com.inventiv.gastropaysdk.repository

import com.inventiv.gastropaysdk.data.Resource
import com.inventiv.gastropaysdk.data.request.LoginRequest
import com.inventiv.gastropaysdk.data.request.OtpConfirmRequest
import com.inventiv.gastropaysdk.data.response.AuthenticationResponse
import com.inventiv.gastropaysdk.data.response.LoginResponse
import kotlinx.coroutines.flow.Flow

internal interface AuthenticationRepository {

    fun login(loginRequest: LoginRequest): Flow<Resource<LoginResponse>>

    fun otpConfirm(otpConfirmRequest: OtpConfirmRequest): Flow<Resource<AuthenticationResponse>>

}