package com.inventiv.gastropaysdk.ui.settings

import androidx.lifecycle.viewModelScope
import com.inventiv.gastropaysdk.common.BaseViewModel
import com.inventiv.gastropaysdk.data.Resource
import com.inventiv.gastropaysdk.data.response.TermsAndConditionResponse
import com.inventiv.gastropaysdk.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

internal class SettingsViewModel(private val repository: SettingsRepository) :
    BaseViewModel() {

    private val _termsState = MutableStateFlow<Resource<TermsAndConditionResponse>>(Resource.Empty)
    val termsState: StateFlow<Resource<TermsAndConditionResponse>> get() = _termsState.asStateFlow()

    fun getTerms() {
        viewModelScope.launch {
            repository.getTerms().collect { response ->
                _termsState.value = response
            }
        }
    }
}