package com.inventiv.gastropaysdk.ui.otp

import androidx.lifecycle.viewModelScope
import com.inventiv.gastropaysdk.common.BaseViewModel
import com.inventiv.gastropaysdk.data.request.LoginRequest
import com.inventiv.gastropaysdk.data.request.OtpConfirmRequest
import com.inventiv.gastropaysdk.data.response.AuthenticationResponse
import com.inventiv.gastropaysdk.data.response.LoginResponse
import com.inventiv.gastropaysdk.model.Resource
import com.inventiv.gastropaysdk.repository.AuthenticationRepository
import com.inventiv.gastropaysdk.utils.blankj.utilcode.util.DeviceUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

internal class OtpViewModel(private val repository: AuthenticationRepository) :
    BaseViewModel() {

    private val _otpState = MutableStateFlow<Resource<AuthenticationResponse>>(Resource.Empty)
    val otpState: StateFlow<Resource<AuthenticationResponse>> get() = _otpState.asStateFlow()

    private val _resendCodeState = MutableStateFlow<Resource<LoginResponse>>(Resource.Empty)
    val resendCodeState: StateFlow<Resource<LoginResponse>> get() = _resendCodeState.asStateFlow()

    fun otpConfirm(smsCode: String, verificationCode: String) {
        val request = OtpConfirmRequest(verificationCode = verificationCode, smsCode = smsCode)
        viewModelScope.launch {
            repository.otpConfirm(request).collect { response ->
                _otpState.value = response
            }
        }
    }

    fun resendCode(phoneNumber: String) {
        val request =
            LoginRequest(gsmNumber = phoneNumber, mobileDeviceToken = DeviceUtils.getAndroidID())
        viewModelScope.launch {
            repository.login(request).collect { response ->
                _resendCodeState.value = response
            }
        }
    }
}