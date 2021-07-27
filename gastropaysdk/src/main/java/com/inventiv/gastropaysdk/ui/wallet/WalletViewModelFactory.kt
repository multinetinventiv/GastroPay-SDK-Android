package com.inventiv.gastropaysdk.ui.wallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.inventiv.gastropaysdk.repository.AuthenticationRepository
import com.inventiv.gastropaysdk.ui.login.LoginViewModel

@Suppress("UNCHECKED_CAST")
internal class WalletViewModelFactory(private val repository: AuthenticationRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return WalletViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}