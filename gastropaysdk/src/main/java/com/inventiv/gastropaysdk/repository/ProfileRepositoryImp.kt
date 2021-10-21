package com.inventiv.gastropaysdk.repository

import com.inventiv.gastropaysdk.api.GastroPayService
import com.inventiv.gastropaysdk.common.BaseRepository
import com.inventiv.gastropaysdk.data.Resource
import com.inventiv.gastropaysdk.data.response.TermsAndConditionResponse
import com.inventiv.gastropaysdk.data.safeFlow
import kotlinx.coroutines.flow.Flow

internal class ProfileRepositoryImp(private val gastroPayService: GastroPayService) :
    ProfileRepository, BaseRepository() {
    /*override fun getTerms(): Flow<Resource<TermsAndConditionResponse>> {
        return safeFlow(
            suspendFun = {
                gastroPayService.getTermsAndCondition()
            }
        )
    }

     */
}