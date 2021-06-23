package com.inventiv.gastropaysdk.ui.merchants

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.inventiv.gastropaysdk.repository.MerchantRepository

@Suppress("UNCHECKED_CAST")
internal class MerchantsViewModelFactory(private val merchantRepository: MerchantRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MerchantsViewModel(merchantRepository) as T
    }
}