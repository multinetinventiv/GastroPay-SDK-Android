package com.inventiv.gastropaysdk.ui.pay.result

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.inventiv.gastropaysdk.repository.PaymentRepository

@Suppress("UNCHECKED_CAST")
internal class PayResultViewModelFactory(private val repository: PaymentRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PayResultViewModel::class.java)) {
            return PayResultViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}