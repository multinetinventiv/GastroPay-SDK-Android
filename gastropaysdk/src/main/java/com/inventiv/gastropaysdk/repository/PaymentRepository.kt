package com.inventiv.gastropaysdk.repository

import com.inventiv.gastropaysdk.data.Resource
import com.inventiv.gastropaysdk.data.request.ConfirmProvisionRequest
import com.inventiv.gastropaysdk.data.request.ProvisionInformationRequest
import com.inventiv.gastropaysdk.data.response.BankCardResponse
import com.inventiv.gastropaysdk.data.response.ProvisionInformationResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

internal interface PaymentRepository {

    fun provisionInformation(
        request: ProvisionInformationRequest
    ): Flow<Resource<ProvisionInformationResponse>>

    fun confirmProvision(request: ConfirmProvisionRequest): Flow<Resource<Response<Unit>>>

    fun bankCards(): Flow<Resource<List<BankCardResponse>>>

}