package com.inventiv.gastropaysdk.data

import android.os.Parcelable
import com.inventiv.gastropaysdk.utils.TransactionType
import kotlinx.parcelize.Parcelize

@Parcelize
internal data class TransactionModel(
    val id: Int,
    val price: String,
    val date: Long,
    val name: String,
    val transactionType: TransactionType,
    val invoiceNumber: String?
) : Parcelable