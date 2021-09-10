package com.inventiv.gastropaysdk.repository

import com.inventiv.gastropaysdk.api.GastroPayService
import com.inventiv.gastropaysdk.common.BaseRepository
import com.inventiv.gastropaysdk.data.Resource
import com.inventiv.gastropaysdk.data.request.ConfirmProvisionRequest
import com.inventiv.gastropaysdk.data.request.ProvisionInformationRequest
import com.inventiv.gastropaysdk.data.response.BankCardResponse
import com.inventiv.gastropaysdk.data.response.ProvisionInformationResponse
import com.inventiv.gastropaysdk.data.safeFlow
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

internal class PaymentRepositoryImp(private val gastroPayService: GastroPayService) :
    PaymentRepository, BaseRepository() {

    override fun provisionInformation(request: ProvisionInformationRequest): Flow<Resource<ProvisionInformationResponse>> {
        return safeFlow(
            suspendFun = {
                gastroPayService.provisionInformation(request)
            }
        )
    }

    override fun confirmProvision(request: ConfirmProvisionRequest): Flow<Resource<Response<Unit>>> {
        return safeFlow(
            suspendFun = {
                gastroPayService.confirmProvision(request)
            }
        )
    }

    override fun bankCards(): Flow<Resource<List<BankCardResponse>>> {
        return safeFlow(
            suspendFun = {
                gastroPayService.getBankCards()
            }
        )
    }

}