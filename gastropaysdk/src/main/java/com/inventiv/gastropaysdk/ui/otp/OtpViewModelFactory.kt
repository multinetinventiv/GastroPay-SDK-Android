package com.inventiv.gastropaysdk.ui.otp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.inventiv.gastropaysdk.repository.AuthenticationRepository

@Suppress("UNCHECKED_CAST")
internal class OtpViewModelFactory(private val repository: AuthenticationRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OtpViewModel::class.java)) {
            return OtpViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}