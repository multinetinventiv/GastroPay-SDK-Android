package com.inventiv.gastropaysdk.ui.merchants.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.inventiv.gastropaysdk.repository.MerchantRepository

@Suppress("UNCHECKED_CAST")
internal class MerchantDetailViewModelFactory(private val merchantRepository: MerchantRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MerchantDetailViewModel(merchantRepository) as T
    }
}