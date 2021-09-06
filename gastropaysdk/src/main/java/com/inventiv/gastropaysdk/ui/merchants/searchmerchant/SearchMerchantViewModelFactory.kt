package com.inventiv.gastropaysdk.ui.merchants.searchmerchant

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.inventiv.gastropaysdk.repository.MerchantRepository

@Suppress("UNCHECKED_CAST")
internal class SearchMerchantViewModelFactory(private val merchantRepository: MerchantRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SearchMerchantViewModelFactory(merchantRepository) as T
    }
}