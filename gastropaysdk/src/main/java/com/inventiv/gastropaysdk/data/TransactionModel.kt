package com.inventiv.gastropaysdk.data

internal data class TransactionModel(
    val price: String,
    val date: Long,
    val name: String,
    val isEarn: Boolean
)