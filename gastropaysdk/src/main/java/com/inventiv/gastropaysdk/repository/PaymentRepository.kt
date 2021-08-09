package com.inventiv.gastropaysdk.repository

import com.inventiv.gastropaysdk.data.Resource
import com.inventiv.gastropaysdk.data.request.ProvisionInformationRequest
import com.inventiv.gastropaysdk.data.response.ProvisionInformationResponse
import kotlinx.coroutines.flow.Flow

internal interface PaymentRepository {

    fun provisionInformation(
        request: ProvisionInformationRequest
    ): Flow<Resource<ProvisionInformationResponse>>

}