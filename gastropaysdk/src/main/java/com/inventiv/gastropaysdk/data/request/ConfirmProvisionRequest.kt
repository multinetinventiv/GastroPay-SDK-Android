package com.inventiv.gastropaysdk.data.request

data class ConfirmProvisionRequest(
    val token: String,
    val cardId: Int,
    val pinCode: String?,
    val useCashback: Boolean
)