package com.inventiv.gastropaysdk.repository

import com.inventiv.gastropaysdk.api.GastroPayService
import com.inventiv.gastropaysdk.common.BaseRepository
import com.inventiv.gastropaysdk.data.request.ProvisionInformationRequest
import com.inventiv.gastropaysdk.data.response.ProvisionInformationResponse
import com.inventiv.gastropaysdk.model.Resource
import com.inventiv.gastropaysdk.model.safeFlow
import kotlinx.coroutines.flow.Flow

internal class PaymentRepositoryImp(private val gastroPayService: GastroPayService) :
    PaymentRepository, BaseRepository() {

    override fun provisionInformation(request: ProvisionInformationRequest): Flow<Resource<ProvisionInformationResponse>> {
        return safeFlow(
            suspendFun = {
                gastroPayService.provisionInformation(request)
            }
        )
    }

}