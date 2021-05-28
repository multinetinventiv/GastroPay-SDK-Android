package com.inventiv.gastropaysdk.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.inventiv.gastropaysdk.activity.MainViewModel
import com.inventiv.gastropaysdk.repository.MainRepository

@Suppress("UNCHECKED_CAST")
internal class ViewModelFactory(private val mainRepository: MainRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(mainRepository) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}