package com.inventiv.gastropaysdk.repository

import com.inventiv.gastropaysdk.data.Resource
import com.inventiv.gastropaysdk.data.response.TermsAndConditionResponse
import kotlinx.coroutines.flow.Flow

internal interface SettingsRepository {
    fun getTerms(): Flow<Resource<TermsAndConditionResponse>>
}