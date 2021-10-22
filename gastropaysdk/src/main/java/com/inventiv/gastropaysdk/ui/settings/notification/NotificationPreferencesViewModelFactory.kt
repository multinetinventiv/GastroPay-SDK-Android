package com.inventiv.gastropaysdk.ui.settings.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.inventiv.gastropaysdk.repository.ProfileRepository

@Suppress("UNCHECKED_CAST")
internal class NotificationPreferencesViewModelFactory(private val profileRepository: ProfileRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NotificationPreferencesViewModel(profileRepository) as T
    }
}