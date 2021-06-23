package com.inventiv.gastropaysdk.merchants

import com.google.common.truth.Truth
import com.inventiv.gastropaysdk.data.model.Resource
import com.inventiv.gastropaysdk.data.model.response.Merchant
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
    fun `merchant repository success test`() = runBlocking {
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
}