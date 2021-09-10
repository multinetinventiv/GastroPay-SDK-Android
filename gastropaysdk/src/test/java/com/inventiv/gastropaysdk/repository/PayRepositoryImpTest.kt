package com.inventiv.gastropaysdk.repository

import com.google.common.truth.Truth
import com.inventiv.gastropaysdk.api.GastroPayService
import com.inventiv.gastropaysdk.data.Resource
import com.inventiv.gastropaysdk.data.request.ConfirmProvisionRequest
import com.inventiv.gastropaysdk.data.request.ProvisionInformationRequest
import com.inventiv.gastropaysdk.data.request.TokenType
import com.inventiv.gastropaysdk.data.response.AmountModel
import com.inventiv.gastropaysdk.data.response.BankCardResponse
import com.inventiv.gastropaysdk.data.response.ProvisionInformationResponse
import com.inventiv.gastropaysdk.utils.ApiUtil
import com.inventiv.gastropaysdk.utils.loadingFalseExpected
import com.inventiv.gastropaysdk.utils.loadingTrueExpected
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import retrofit2.Response

class PayRepositoryImpTest {

    private val service: GastroPayService = mock()

    private val repository = PaymentRepositoryImp(service)

    //region provisionInformation
    @Test
    fun `provisionInformation repository success test`() = runBlocking {
        // Given
        val expected = ProvisionInformationResponse(
            "1",
            "",
            AmountModel(0.0, "", "", ""),
            null,
            null,
            AmountModel(0.0, "", "", ""),
            AmountModel(0.0, "", "", ""),
            AmountModel(0.0, "", "", ""),
            "",
            null,
            null,
        )

        val request = ProvisionInformationRequest(
            token = "123456",
            isBonusUsed = false,
            tokenType = TokenType.QR.value
        )

        // When
        whenever(service.provisionInformation(request)).thenReturn(expected)

        // Then
        repository.provisionInformation(request).collectIndexed { index, response ->
            if (index == 0) {
                response.loadingTrueExpected()
            }
            if (index == 1) {
                Truth.assertThat(response is Resource.Success)
                val success = response as Resource.Success
                Truth.assertThat(success.data.merchantId).isEqualTo("1")
            }
            if (index == 2) {
                response.loadingFalseExpected()
            }
        }
    }

    @Test
    fun `provisionInformation repository error test`() = runBlocking {
        // Given
        val expected = ApiUtil.dummyHttpException(
            response = ProvisionInformationResponse::class.java,
            code = 400
        )

        val request = ProvisionInformationRequest(
            token = "123456",
            isBonusUsed = false,
            tokenType = TokenType.QR.value
        )

        // When
        whenever(service.provisionInformation(request)).thenThrow(expected)

        // Then
        repository.provisionInformation(request).collectIndexed { index, response ->
            when (index) {
                0 -> {
                    response.loadingTrueExpected()
                }
                1 -> {
                    Truth.assertThat(response is Resource.Error)
                    val error = response as Resource.Error
                    Truth.assertThat(error.apiError.code).isEqualTo(400)
                }
                2 -> {
                    response.loadingFalseExpected()
                }
            }
        }
    }
    //endregion provision_information

    //region confirmProvision
    @Test
    fun `confirmProvision repository success test`() = runBlocking {
        // Given
        val expected = Response.success(Unit)

        val request = ConfirmProvisionRequest(
            token = "",
            cardId = 1,
            pinCode = null,
            useCashback = false
        )

        // When
        whenever(service.confirmProvision(request)).thenReturn(expected)

        // Then
        repository.confirmProvision(request).collectIndexed { index, response ->
            if (index == 0) {
                response.loadingTrueExpected()
            }
            if (index == 1) {
                Truth.assertThat(response is Resource.Success)
                val success = response as Resource.Success
                Truth.assertThat(success.data.code()).isEqualTo(200)
                Truth.assertThat(success.data).isEqualTo(expected)
            }
            if (index == 2) {
                response.loadingFalseExpected()
            }
        }
    }

    @Test
    fun `confirmProvision repository error test`() = runBlocking {
        // Given
        val expected = ApiUtil.dummyHttpException(
            response = Unit::class.java,
            code = 400
        )

        val request = ConfirmProvisionRequest(
            token = "",
            cardId = 1,
            pinCode = null,
            useCashback = false
        )

        // When
        whenever(service.confirmProvision(request)).thenThrow(expected)

        // Then
        repository.confirmProvision(request).collectIndexed { index, response ->
            when (index) {
                0 -> {
                    response.loadingTrueExpected()
                }
                1 -> {
                    Truth.assertThat(response is Resource.Error)
                    val error = response as Resource.Error
                    Truth.assertThat(error.apiError.code).isEqualTo(400)
                }
                2 -> {
                    response.loadingFalseExpected()
                }
            }
        }
    }
    //endregion confirmProvision

    //region bankCards
    @Test
    fun `bankCards repository success test`() = runBlocking {
        // Given
        val expected = listOf(
            BankCardResponse(
                id = 1,
                number = "12345678",
                alias = "",
                binNumber = "",
                isDefault = true,
                bankImageUrl = null,
                paymentSourceImageUrl = null
            ),
            BankCardResponse(
                id = 2,
                number = "987654321",
                alias = "",
                binNumber = "",
                isDefault = false,
                bankImageUrl = null,
                paymentSourceImageUrl = null
            )
        )

        // When
        whenever(service.getBankCards()).thenReturn(expected)

        // Then
        repository.bankCards().collectIndexed { index, response ->
            if (index == 0) {
                response.loadingTrueExpected()
            }
            if (index == 1) {
                Truth.assertThat(response is Resource.Success)
                val success = response as Resource.Success
                Truth.assertThat(success.data).isEqualTo(expected)
                Truth.assertThat(success.data[0].isDefault).isEqualTo(true)
                Truth.assertThat(success.data[1].isDefault).isEqualTo(false)
            }
            if (index == 2) {
                response.loadingFalseExpected()
            }
        }
    }

    @Test
    fun `bankCards repository error test`() = runBlocking {
        // Given
        val expected = ApiUtil.dummyHttpException(
            response = List::class.java,
            code = 403
        )

        // When
        whenever(service.getBankCards()).thenThrow(expected)

        // Then
        repository.bankCards().collectIndexed { index, response ->
            when (index) {
                0 -> {
                    response.loadingTrueExpected()
                }
                1 -> {
                    Truth.assertThat(response is Resource.Error)
                    val error = response as Resource.Error
                    Truth.assertThat(error.apiError.code).isEqualTo(403)
                }
                2 -> {
                    response.loadingFalseExpected()
                }
            }
        }
    }
    //endregion bankCards

}