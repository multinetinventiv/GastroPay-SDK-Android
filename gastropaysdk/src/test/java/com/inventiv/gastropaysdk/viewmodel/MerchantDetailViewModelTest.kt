package com.inventiv.gastropaysdk.viewmodel

import com.google.common.truth.Truth
import com.inventiv.gastropaysdk.data.Resource
import com.inventiv.gastropaysdk.data.response.MerchantDetailResponse
import com.inventiv.gastropaysdk.repository.MerchantRepository
import com.inventiv.gastropaysdk.ui.merchants.detail.MerchantDetailViewModel
import com.inventiv.gastropaysdk.utils.MainCoroutineScopeRule
import com.inventiv.gastropaysdk.utils.emptyExpected
import com.inventiv.gastropaysdk.utils.loadingFalseExpected
import com.inventiv.gastropaysdk.utils.loadingTrueExpected
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
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
class MerchantDetailViewModelTest {

    @get:Rule
    val coroutineRule = MainCoroutineScopeRule()

    private val repository: MerchantRepository = mock()

    private var viewModel: MerchantDetailViewModel = mock()

    @Before
    fun setUp() {
        viewModel = MerchantDetailViewModel(repository)
    }

    @Test
    fun `viewmodel success test`() = runBlockingTest {

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
        // Given
        val flow = flow {
            emit(Resource.Loading(true))
            emit(Resource.Success(expected))
            emit(Resource.Loading(false))
        }

        // When
        whenever(repository.getMerchantDetail("1")).thenReturn(flow)

        launch {
            val list: List<Resource<MerchantDetailResponse>> = viewModel.uiState.take(4).toList()

            list[0].emptyExpected()
            list[1].loadingTrueExpected()

            val response = list[2] as Resource.Success
            Truth.assertThat(response.data).isEqualTo(expected)

            list[3].loadingFalseExpected()
        }

        viewModel.getMerchantDetail("1")
    }

}