package com.inventiv.gastropaysdk.shared

interface GastroPaySdkListener {
    fun onInitialized(isInitialized: Boolean) {}
    fun onAuthTokenReceived(authToken: String) {} //TODO will be updated after refresh-token flow
    fun onSDKClosed() {}
}