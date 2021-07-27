package com.inventiv.gastropaysdk.utils

import com.google.common.truth.Truth
import com.inventiv.gastropaysdk.model.Resource


fun <T> Resource<T>.emptyExpected() {
    Truth.assertThat(this is Resource.Empty)
}

fun <T> Resource<T>.loadingTrueExpected() {
    Truth.assertThat(this is Resource.Loading)
    val loading = this as Resource.Loading
    Truth.assertThat(loading.isLoading).isTrue()
}

fun <T> Resource<T>.loadingFalseExpected() {
    Truth.assertThat(this is Resource.Loading)
    val loading = this as Resource.Loading
    Truth.assertThat(loading.isLoading).isFalse()
}