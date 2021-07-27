package com.inventiv.gastropaysdk.repository

import com.google.common.truth.Truth
import com.inventiv.gastropaysdk.api.GastroPayService
import com.inventiv.gastropaysdk.data.request.LoginRequest
import com.inventiv.gastropaysdk.data.request.OtpConfirmRequest
import com.inventiv.gastropaysdk.data.response.AuthenticationResponse
import com.inventiv.gastropaysdk.data.response.LoginResponse
import com.inventiv.gastropaysdk.model.Resource
import com.inventiv.gastropaysdk.utils.ApiUtil
import com.inventiv.gastropaysdk.utils.loadingFalseExpected
import com.inventiv.gastropaysdk.utils.loadingTrueExpected
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class AuthenticationRepositoryImpTest {

    private val service: GastroPayService = mock()

    private val repository = AuthenticationRepositoryImp(service)

    @Test
    fun `login repository success test`() = runBlocking {
        // Given
        val expected = LoginResponse(
            verificationCode = "005DI+3FQIvuS6",
            endTime = "1626193843"
        )

        val loginRequest = LoginRequest(
            gsmNumber = "5555555555",
            mobileDeviceToken = "9240203ab2eb3297"
        )

        // When
        whenever(service.login(loginRequest)).thenReturn(expected)

        // Then
        repository.login(loginRequest).collectIndexed { index, response ->
            if (index == 0) {
                response.loadingTrueExpected()
            }
            if (index == 1) {
                Truth.assertThat(response is Resource.Success)
                val success = response as Resource.Success
                Truth.assertThat(success.data.endTime).isEqualTo("1626193843")
            }
            if (index == 2) {
                response.loadingFalseExpected()
            }
        }
    }

    @Test
    fun `login repository error test`() = runBlocking {
        // Given
        val expected = ApiUtil.dummyHttpException(
            response = LoginResponse::class.java,
            code = 400
        )

        val loginRequest = LoginRequest(
            gsmNumber = "5555555555",
            mobileDeviceToken = "9240203ab2eb3297"
        )

        // When
        whenever(service.login(loginRequest)).thenThrow(expected)

        // Then
        repository.login(loginRequest).collectIndexed { index, response ->
            if (index == 0) {
                response.loadingTrueExpected()
            }
            if (index == 1) {
                Truth.assertThat(response is Resource.Error)
                val error = response as Resource.Error
                Truth.assertThat(error.apiError.code).isEqualTo(400)
            }
            if (index == 2) {
                response.loadingFalseExpected()
            }
        }
    }

    @Test
    fun `otpConfirm repository success test`() = runBlocking {
        // Given
        val expected = AuthenticationResponse(
            userToken = "15dadb9da0e446",
            refreshToken = "1dfd6237df124e",
            expirationTime = "1657730517",
            refreshTokenExpirationTime = "1657730517"
        )

        val otpConfirmRequest = OtpConfirmRequest(
            smsCode = "123456",
            verificationCode = "005DI+3FQIvuS6"
        )

        // When
        whenever(service.otpConfirm(otpConfirmRequest)).thenReturn(expected)

        // Then
        repository.otpConfirm(otpConfirmRequest).collectIndexed { index, response ->
            if (index == 0) {
                response.loadingTrueExpected()
            }
            if (index == 1) {
                Truth.assertThat(response is Resource.Success)
                val success = response as Resource.Success
                Truth.assertThat(success.data.refreshToken).isEqualTo("1dfd6237df124e")
            }
            if (index == 2) {
                response.loadingFalseExpected()
            }
        }
    }

    @Test
    fun `otpConfirm repository error test`() = runBlocking {
        // Given
        val expected = ApiUtil.dummyHttpException(
            response = AuthenticationResponse::class.java,
            code = 400
        )

        val otpConfirmRequest = OtpConfirmRequest(
            smsCode = "123456",
            verificationCode = "005DI+3FQIvuS6"
        )

        // When
        whenever(service.otpConfirm(otpConfirmRequest)).thenThrow(expected)

        // Then
        repository.otpConfirm(otpConfirmRequest).collectIndexed { index, response ->
            if (index == 0) {
                response.loadingTrueExpected()
            }
            if (index == 1) {
                Truth.assertThat(response is Resource.Error)
                val error = response as Resource.Error
                Truth.assertThat(error.apiError.code).isEqualTo(400)
            }
            if (index == 2) {
                response.loadingFalseExpected()
            }
        }
    }
}