package com.inventiv.gastropaysdk.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.inventiv.gastropaysdk.repository.AuthenticationRepository

@Suppress("UNCHECKED_CAST")
internal class LoginViewModelFactory(private val repository: AuthenticationRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}