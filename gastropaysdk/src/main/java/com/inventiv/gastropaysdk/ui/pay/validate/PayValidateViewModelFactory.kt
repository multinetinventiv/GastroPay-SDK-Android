package com.inventiv.gastropaysdk.ui.pay.validate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.inventiv.gastropaysdk.repository.PaymentRepository

@Suppress("UNCHECKED_CAST")
internal class PayValidateViewModelFactory(private val repository: PaymentRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PayValidateViewModel::class.java)) {
            return PayValidateViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}