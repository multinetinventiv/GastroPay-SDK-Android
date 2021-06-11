package com.inventiv.gastropaysdk.ui.merchants

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.inventiv.gastropaysdk.repository.MainRepository

@Suppress("UNCHECKED_CAST")
internal class MerchantsViewModelFactory(private val mainRepository: MainRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MerchantsViewModel(mainRepository) as T
    }
}