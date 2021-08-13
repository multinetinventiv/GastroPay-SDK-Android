package com.inventiv.gastropaysdk.data.response

internal data class LastTransactionsResponse(
    val id: Int,
    val merchantName: String,
    val transactionAmount: AmountModel,
    val transactionDate: Long,
)

internal data class AmountModel(
    val value: Double,
    val currency: String,
    val sign: String,
    val displayValue: String
)