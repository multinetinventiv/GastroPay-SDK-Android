package com.inventiv.gastropaysdk.merchants

import com.google.common.truth.Truth
import com.inventiv.gastropaysdk.api.GastroPayService
import com.inventiv.gastropaysdk.data.response.MerchantDetailResponse
import com.inventiv.gastropaysdk.data.response.MerchantResponse
import com.inventiv.gastropaysdk.data.response.MerchantsResponse
import com.inventiv.gastropaysdk.model.Resource
import com.inventiv.gastropaysdk.repository.MerchantRepositoryImp
import com.inventiv.gastropaysdk.utils.loadingFalseExpected
import com.inventiv.gastropaysdk.utils.loadingTrueExpected
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class MerchantRepositoryTest {

    private val service: GastroPayService = mock()

    private val repository = MerchantRepositoryImp(service)

    @Test
    fun `getMerchants repository success test`() = runBlocking {
        // Given
        val expected = MerchantsResponse(
            merchants = listOf(MerchantResponse("", "", 0.0, 0.0, 0, null, null, null, null, null)),
            isLastPage = true
        )

        // When
        whenever(service.merchantsInfo(0.0, 0.0, null, false, null, 0)).thenReturn(expected)

        // Then
        repository.getMerchants(0.0, 0.0, null, null, 0).collectIndexed { index, response ->
            if (index == 0) {
                response.loadingTrueExpected()
            }
            if (index == 1) {
                Truth.assertThat(response is Resource.Success)
                val success = response as Resource.Success
                Truth.assertThat(success.data).isEqualTo(expected)
            }
            if (index == 2) {
                response.loadingFalseExpected()
            }
        }

    }

    @Test
    fun `getMerchantDetail repository success test`() = runBlocking {
        // Given
        val expected = MerchantDetailResponse(
            merchantId = "1",
            tags = arrayListOf(),
            pageContent = null,
            address = null,
            name = "Test",
            logoUrl = null,
            images = arrayListOf(),
            isBonusPoint = false,
            rewardPercentage = null,
            distance = null,
            latitude = 0.0,
            longitude = 0.0,
            note = null,
            rate = 0f,
            showcaseImageUrl = null,
            phoneNumber = null,
            gsmNumber = null
        )

        val flow = flow {
            emit(Resource.Loading(true))
            emit(Resource.Success(expected))
            emit(Resource.Loading(false))
        }

        // When
        whenever(repository.getMerchantDetail("1")).thenReturn(flow)

        // Then
        repository.getMerchantDetail("1").collectIndexed { index, response ->
            if (index == 0) {
                response.loadingTrueExpected()
            }
            if (index == 1) {
                Truth.assertThat(response is Resource.Success)
                val success = response as Resource.Success
                Truth.assertThat(success.data).isEqualTo(expected)
            }
            if (index == 2) {
                response.loadingFalseExpected()
            }
        }

    }
}