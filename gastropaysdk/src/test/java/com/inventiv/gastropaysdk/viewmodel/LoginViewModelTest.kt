package com.inventiv.gastropaysdk.viewmodel

import com.google.common.truth.Truth
import com.inventiv.gastropaysdk.api.GastroPayService
import com.inventiv.gastropaysdk.data.Resource
import com.inventiv.gastropaysdk.data.request.LoginRequest
import com.inventiv.gastropaysdk.data.response.LoginResponse
import com.inventiv.gastropaysdk.repository.AuthenticationRepositoryImp
import com.inventiv.gastropaysdk.ui.login.LoginViewModel
import com.inventiv.gastropaysdk.utils.MainCoroutineScopeRule
import com.inventiv.gastropaysdk.utils.emptyExpected
import com.inventiv.gastropaysdk.utils.loadingFalseExpected
import com.inventiv.gastropaysdk.utils.loadingTrueExpected
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import kotlin.time.ExperimentalTime

@ExperimentalTime
@ExperimentalCoroutinesApi
class LoginViewModelTest {

    @get:Rule
    val coroutineRule = MainCoroutineScopeRule()

    private lateinit var viewModel: LoginViewModel

    private var service: GastroPayService = mock()

    private val repository = AuthenticationRepositoryImp(service)

    @Before
    fun setUp() {
        viewModel = LoginViewModel(repository)
    }

    @Test
    fun `login viewmodel success test`() = runBlockingTest {

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

        launch {
            val list: List<Resource<LoginResponse>> = viewModel.loginState.take(4).toList()

            list[0].emptyExpected()
            list[1].loadingTrueExpected()

            val response = list[2] as Resource.Success
            Truth.assertThat(response.data).isEqualTo(expected)

            list[3].loadingFalseExpected()
        }

        viewModel.login(
            phoneNumber = "5555555555",
            mobileDeviceToken = "9240203ab2eb3297"
        )
    }

}