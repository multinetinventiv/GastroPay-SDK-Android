package com.inventiv.gastropaysdk.repository

import com.google.common.truth.Truth
import com.inventiv.gastropaysdk.api.GastroPayService
import com.inventiv.gastropaysdk.data.Resource
import com.inventiv.gastropaysdk.data.request.ProvisionInformationRequest
import com.inventiv.gastropaysdk.data.request.TokenType
import com.inventiv.gastropaysdk.data.response.ProvisionInformationResponse
import com.inventiv.gastropaysdk.utils.loadingFalseExpected
import com.inventiv.gastropaysdk.utils.loadingTrueExpected
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class PayRepositoryImpTest {

    private val service: GastroPayService = mock()

    private val repository = PaymentRepositoryImp(service)

    @Test
    fun `provision_information repository success test`() = runBlocking {
        // Given
        val expected = ProvisionInformationResponse(
            "1",
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
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

}