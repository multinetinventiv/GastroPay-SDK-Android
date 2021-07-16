package com.inventiv.gastropaysdk.shared

interface GastroPaySdkListener {
    fun onInitialized(isInitialized: Boolean) {}
    fun onAuthTokenReceived(authToken: String) {} //TODO will be update after refresh-token flow
    fun onSDKClosed() {}
}