package com.inventiv.gastropaysdk.utils

fun String.isValidPhone(): Boolean {
    return if (this.isEmpty()) {
        false
    } else {
        this.length == 16
    }
}