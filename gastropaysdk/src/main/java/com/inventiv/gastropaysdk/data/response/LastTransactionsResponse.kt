package com.inventiv.gastropaysdk.data.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

internal data class LastTransactionsResponse(
    val id: Int,
    val merchantName: String,
    val transactionAmount: AmountModel,
    val transactionDate: Long,
)

@Parcelize
internal data class AmountModel(
    val value: Double,
    val currency: String,
    val sign: String,
    val displayValue: String
) : Parcelable