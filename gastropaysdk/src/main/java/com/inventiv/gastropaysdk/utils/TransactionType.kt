package com.inventiv.gastropaysdk.utils

internal enum class TransactionType(val type: Int) {
    DEPOSIT(10),
    WITHDRAW(20),
    CANCEL_DEPOSIT(42),
    CANCEL_WITHDRAW(44);

    companion object {
        fun find(type: Int): TransactionType {
            return when (type) {
                DEPOSIT.type -> {
                    DEPOSIT
                }
                WITHDRAW.type -> {
                    WITHDRAW
                }
                CANCEL_DEPOSIT.type -> {
                    CANCEL_DEPOSIT
                }
                CANCEL_WITHDRAW.type -> {
                    CANCEL_WITHDRAW
                }
                else -> DEPOSIT
            }
        }
    }
}