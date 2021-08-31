package com.inventiv.gastropaysdk.ui.pay

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.inventiv.gastropaysdk.repository.PaymentRepository

@Suppress("UNCHECKED_CAST")
internal class PayViewModelFactory(private val repository: PaymentRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PayViewModel::class.java)) {
            return PayViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}