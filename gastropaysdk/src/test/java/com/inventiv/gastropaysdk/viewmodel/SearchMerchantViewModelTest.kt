package com.inventiv.gastropaysdk.viewmodel

import com.google.common.truth.Truth
import com.inventiv.gastropaysdk.api.GastroPayService
import com.inventiv.gastropaysdk.data.Resource
import com.inventiv.gastropaysdk.data.response.*
import com.inventiv.gastropaysdk.repository.MerchantRepositoryImp
import com.inventiv.gastropaysdk.ui.merchants.searchmerchant.SearchMerchantViewModel
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
class SearchMerchantViewModelTest {

    @get:Rule
    val coroutineRule = MainCoroutineScopeRule()

    private var service: GastroPayService = mock()

    private val repository = MerchantRepositoryImp(service)

    private lateinit var viewModel: SearchMerchantViewModel

    @Before
    fun setUp() {
        viewModel = SearchMerchantViewModel(repository)
    }

    @Test
    fun `getSearchCriteria viewmodel success test`() = runBlockingTest {

        // Given
        val expected = listOf(
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
        whenever(service.searchCriterias(null)).thenReturn(expected)

        launch {
            val list: List<Resource<List<TagGroupResponse>>> =
                viewModel.uiStateSearchCriteria.take(4).toList()

            list[0].emptyExpected()
            list[1].loadingTrueExpected()

            val response = list[2] as Resource.Success
            Truth.assertThat(response.data).isEqualTo(expected)

            list[3].loadingFalseExpected()
        }

        viewModel.getSearchCriteria(null)
    }

    @Test
    fun `getCities viewmodel success test`() = runBlockingTest {

        // Given
        val expected = CitiesResponse(
            cities = arrayListOf(
                City(64, "İstanbul"),
                City(47, "Karabük")
            )
        )

        // When
        whenever(service.cities()).thenReturn(expected)

        launch {
            val list: List<Resource<CitiesResponse>> =
                viewModel.uiStateCities.take(4).toList()

            list[0].emptyExpected()
            list[1].loadingTrueExpected()

            val response = list[2] as Resource.Success
            Truth.assertThat(response.data).isEqualTo(expected)

            list[3].loadingFalseExpected()
        }

        viewModel.getCities()
    }

}