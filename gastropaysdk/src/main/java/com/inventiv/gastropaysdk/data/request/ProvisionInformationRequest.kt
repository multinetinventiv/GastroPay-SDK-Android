package com.inventiv.gastropaysdk.data.request

data class ProvisionInformationRequest(
    val token: String,
    val isBonusUsed: Boolean,
    val tokenType: Int = TokenType.QR.value
)

enum class TokenType(val value: Int) {
    QR(0), CODE(1)
}