package com.inventiv.gastropaysdk.merchants

import com.google.common.truth.Truth
import com.inventiv.gastropaysdk.data.model.Resource
import com.inventiv.gastropaysdk.data.model.response.Merchant
import com.inventiv.gastropaysdk.data.model.response.MerchantDetail
import com.inventiv.gastropaysdk.data.model.response.MerchantPaging
import com.inventiv.gastropaysdk.repository.MerchantRepository
import com.inventiv.gastropaysdk.utils.loadingFalseExpected
import com.inventiv.gastropaysdk.utils.loadingTrueExpected
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class MerchantRepositoryTest {

    private val repository: MerchantRepository = mock()

    @Test
    fun `getMerchants repository success test`() = runBlocking {
        // Given
        val expected = MerchantPaging(
            merchants = listOf(Merchant("", "", 0.0, 0.0, 0, null, null, null, null, null)),
            isLastPage = true,
            pageIndex = 1
        )

        val flow = flow {
            emit(Resource.Loading(true))
            emit(Resource.Success(expected))
            emit(Resource.Loading(false))
        }

        // When
        whenever(repository.getMerchants(0.0, 0.0, null, null, 1)).thenReturn(flow)

        // Then
        repository.getMerchants(0.0, 0.0, null, null, 1).collectIndexed { index, response ->
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
        val expected = MerchantDetail(
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