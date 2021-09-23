package com.inventiv.gastropaysdk.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.inventiv.gastropaysdk.repository.SettingsRepository

@Suppress("UNCHECKED_CAST")
internal class SettingsViewModelFactory(private val settingsRepository: SettingsRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SettingsViewModel(settingsRepository) as T
    }
}