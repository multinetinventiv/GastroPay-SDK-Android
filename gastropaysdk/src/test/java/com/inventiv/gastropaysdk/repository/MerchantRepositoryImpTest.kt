package com.inventiv.gastropaysdk.repository

import com.google.common.truth.Truth
import com.inventiv.gastropaysdk.api.GastroPayService
import com.inventiv.gastropaysdk.data.Resource
import com.inventiv.gastropaysdk.data.response.*
import com.inventiv.gastropaysdk.utils.ApiUtil
import com.inventiv.gastropaysdk.utils.loadingFalseExpected
import com.inventiv.gastropaysdk.utils.loadingTrueExpected
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class MerchantRepositoryImpTest {

    private val service: GastroPayService = mock()

    private val repository = MerchantRepositoryImp(service)

    @Test
    fun `getMerchants repository success test`() = runBlocking {
        // Given
        val expected = MerchantListResponse(
            merchants = listOf(
                MerchantResponse(
                    "",
                    "",
                    0.0,
                    0.0,
                    0,
                    null,
                    null,
                    null,
                    null,
                    null
                )
            ),
            isLastPage = true
        )

        // When
        whenever(
            service.merchantsInfo(
                0.0, 0.0, null, false, null, null, 0
            )
        ).thenReturn(expected)

        // Then
        repository.getMerchants(
            0.0, 0.0, null, null, null, 0
        ).collectIndexed { index, response ->
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
    fun `getMerchants repository error test`() = runBlocking {
        // Given
        val expected = ApiUtil.dummyHttpException(
            response = MerchantListResponse::class.java,
            code = 400
        )

        // When
        whenever(
            service.merchantsInfo(
                0.0,
                0.0,
                null,
                false,
                null,
                null,
                0
            )
        ).thenThrow(expected)

        // Then
        repository.getMerchants(
            0.0,
            0.0,
            null,
            null,
            null,
            0
        ).collectIndexed { index, response ->
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

        // When
        whenever(service.merchantDetail("1")).thenReturn(expected)

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

    @Test
    fun `getMerchantDetail repository error test`() = runBlocking {
        // Given
        val expected = ApiUtil.dummyHttpException(
            response = MerchantDetailResponse::class.java,
            code = 400
        )

        // When
        whenever(service.merchantDetail("1")).thenThrow(expected)

        // Then
        repository.getMerchantDetail("1").collectIndexed { index, response ->
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
    fun `getCities repository success test`() = runBlocking {
        // Given
        val expected = CitiesResponse(
            cities = arrayListOf(
                City(64, "İstanbul"),
                City(47, "Karabük")
            )
        )

        // When
        whenever(service.cities()).thenReturn(expected)

        // Then
        repository.getCities().collectIndexed { index, response ->
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
    fun `getCities repository error test`() = runBlocking {
        // Given
        val expected = ApiUtil.dummyHttpException(
            response = MerchantListResponse::class.java,
            code = 400
        )

        // When
        whenever(service.cities()).thenThrow(expected)

        // Then
        repository.getCities().collectIndexed { index, response ->
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
    fun `getSearchCriteria repository success test`() = runBlocking {
        // Given
        val expected = arrayListOf(
            TagGroupResponse(
                "5e5f9470d65e8323f457db5s",
                "Kategoriler",
                "Categories",
                arrayListOf(
                    Tag(
                        "5fdb630c3689ab203006d642",
                        "Ankara Mutfağı",
                        ImageResponse(null, null, false, 0, null),
                        false
                    )
                )
            )
        )

        // When
        whenever(service.searchCriterias("64")).thenReturn(expected)

        // Then
        repository.getSearchCriteria("64").collectIndexed { index, response ->
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
    fun `getSearchCriteria repository error test`() = runBlocking {
        // Given
        val expected = ApiUtil.dummyHttpException(
            response = MerchantListResponse::class.java,
            code = 400
        )

        // When
        whenever(service.searchCriterias(null)).thenThrow(expected)

        // Then
        repository.getSearchCriteria(null).collectIndexed { index, response ->
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