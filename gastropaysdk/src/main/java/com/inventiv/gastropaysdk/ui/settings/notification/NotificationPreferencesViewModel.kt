package com.inventiv.gastropaysdk.ui.settings.notification

import androidx.lifecycle.viewModelScope
import com.inventiv.gastropaysdk.common.BaseViewModel
import com.inventiv.gastropaysdk.data.Resource
import com.inventiv.gastropaysdk.data.request.ConfirmProvisionRequest
import com.inventiv.gastropaysdk.data.request.NotificationPreferencesRequest
import com.inventiv.gastropaysdk.data.request.ProvisionInformationRequest
import com.inventiv.gastropaysdk.data.response.BankCardResponse
import com.inventiv.gastropaysdk.data.response.NotificationPreferencesResponse
import com.inventiv.gastropaysdk.data.response.ProvisionInformationResponse
import com.inventiv.gastropaysdk.data.response.TermsAndConditionResponse
import com.inventiv.gastropaysdk.repository.ProfileRepository
import com.inventiv.gastropaysdk.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.Response

internal class NotificationPreferencesViewModel(private val repository: ProfileRepository) :
    BaseViewModel() {

    private val _notificationPreferencesState = MutableStateFlow<Resource<List<NotificationPreferencesResponse>>>(Resource.Empty)
    val notificationPreferencesState: StateFlow<Resource<List<NotificationPreferencesResponse>>>
        get() = _notificationPreferencesState.asStateFlow()

    private val _updateNotificationPreferencesState = MutableStateFlow<Resource<Response<Unit>>>(Resource.Empty)
    val updateNotificationPreferencesState: StateFlow<Resource<Response<Unit>>>
        get() = _updateNotificationPreferencesState.asStateFlow()


    fun notificationPreferences() {
        viewModelScope.launch {
            repository.notificationPreferences().collect { response ->
                _notificationPreferencesState.value = response
            }
        }
    }

    fun updateNotificationPreferences(channel: Int, state: Int) {
        val request = NotificationPreferencesRequest(channel = channel, newState = state)
        viewModelScope.launch {
            repository.updateNotificationPreferences(request).collect { response ->
                _updateNotificationPreferencesState.value = response
            }
        }
    }


}
