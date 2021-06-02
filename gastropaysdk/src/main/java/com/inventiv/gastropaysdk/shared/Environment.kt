package com.inventiv.gastropaysdk.shared

enum class Environment(
    internal var baseUrl: String,
    internal var apiServicePath: String,
    internal val encryptedBaseUrl: ByteArray,
    internal val encryptedApiServicePath: ByteArray
) {
    DEV(
        baseUrl = "",
        apiServicePath = "",
        encryptedBaseUrl = byteArrayOf(-1),
        encryptedApiServicePath = byteArrayOf(-1)
    ),
    PILOT(
        baseUrl = "",
        apiServicePath = "",
        encryptedBaseUrl = byteArrayOf(-1),
        encryptedApiServicePath = byteArrayOf(-1)
    ),
    TEST(
        baseUrl = "",
        apiServicePath = "",
        encryptedBaseUrl = byteArrayOf(-1),
        encryptedApiServicePath = byteArrayOf(-1)
    ),
    PRODUCTION(
        baseUrl = "",
        apiServicePath = "",
        encryptedBaseUrl = byteArrayOf(-1),
        encryptedApiServicePath = byteArrayOf(-1)
    )
}