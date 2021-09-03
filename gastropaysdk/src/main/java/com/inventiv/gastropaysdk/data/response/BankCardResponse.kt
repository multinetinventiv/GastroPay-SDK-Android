package com.inventiv.gastropaysdk.data.response

data class BankCardResponse(
    val id: Int,
    val number: String,
    val alias: String,
    val binNumber: String,
    var isDefault: Boolean,
    val bankImageUrl: String?,
    val paymentSourceImageUrl: String?
)