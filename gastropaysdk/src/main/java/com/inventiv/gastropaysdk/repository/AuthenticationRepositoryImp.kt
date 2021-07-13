package com.inventiv.gastropaysdk.repository

import com.inventiv.gastropaysdk.api.GastroPayService
import com.inventiv.gastropaysdk.common.BaseRepository
import com.inventiv.gastropaysdk.data.request.LoginRequest
import com.inventiv.gastropaysdk.data.request.OtpConfirmRequest
import com.inventiv.gastropaysdk.data.response.AuthenticationResponse
import com.inventiv.gastropaysdk.data.response.LoginResponse
import com.inventiv.gastropaysdk.model.Resource
import com.inventiv.gastropaysdk.model.safeFlow
import kotlinx.coroutines.flow.Flow

internal class AuthenticationRepositoryImp(private val gastroPayService: GastroPayService) :
    AuthenticationRepository,
    BaseRepository() {

    override fun login(loginRequest: LoginRequest): Flow<Resource<LoginResponse>> {
        return safeFlow(
            suspendFun = {
                gastroPayService.login(loginRequest)
            }
        )
    }

    override fun otpConfirm(otpConfirmRequest: OtpConfirmRequest): Flow<Resource<AuthenticationResponse>> {
        return safeFlow(
            suspendFun = {
                gastroPayService.otpConfirm(otpConfirmRequest)
            }
        )
    }

}