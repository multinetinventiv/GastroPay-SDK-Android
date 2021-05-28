package com.inventiv.gastropaysdk.shared

sealed class GastroPaySdkException(message: String) : RuntimeException(message) {
    class SecurityException(message: String) : GastroPaySdkException(message)
}
