package com.inventiv.gastropaysdk.repository

import com.inventiv.gastropaysdk.data.request.LoginRequest
import com.inventiv.gastropaysdk.data.response.LoginResponse
import com.inventiv.gastropaysdk.model.Resource
import kotlinx.coroutines.flow.Flow

internal interface AuthenticationRepository {

    fun login(loginRequest: LoginRequest): Flow<Resource<LoginResponse>>

}