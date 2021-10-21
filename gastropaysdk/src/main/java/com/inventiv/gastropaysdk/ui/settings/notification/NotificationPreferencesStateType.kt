package com.inventiv.gastropaysdk.ui.settings.notification

import com.google.gson.annotations.SerializedName

enum class NotificationPreferencesStateType(var value: String) {
    @SerializedName("0")
    OFF("0"),

    @SerializedName("1")
    ON("1");

    companion object {
        fun getState(value: String): NotificationPreferencesStateType {
            values().forEach {
                if (it.value == value) {
                    return it
                }
            }
            throw IllegalArgumentException("No item found for value: $value")
        }
    }
}