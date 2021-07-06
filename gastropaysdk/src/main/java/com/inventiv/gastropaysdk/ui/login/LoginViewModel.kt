package com.inventiv.gastropaysdk.ui.login

import androidx.lifecycle.viewModelScope
import com.inventiv.gastropaysdk.common.BaseViewModel
import com.inventiv.gastropaysdk.data.request.LoginRequest
import com.inventiv.gastropaysdk.data.response.LoginResponse
import com.inventiv.gastropaysdk.model.Resource
import com.inventiv.gastropaysdk.repository.AuthenticationRepository
import com.inventiv.gastropaysdk.utils.blankj.utilcode.util.DeviceUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

internal class LoginViewModel(private val repository: AuthenticationRepository) :
    BaseViewModel() {

    private val _loginState = MutableStateFlow<Resource<LoginResponse>>(Resource.Empty)
    val loginState: StateFlow<Resource<LoginResponse>> get() = _loginState.asStateFlow()

    fun login(phoneNumber: String) {
        val request =
            LoginRequest(gsmNumber = phoneNumber, mobileDeviceToken = DeviceUtils.getAndroidID())
        viewModelScope.launch {
            repository.login(request).collect { response ->
                _loginState.value = response
            }
        }
    }
}