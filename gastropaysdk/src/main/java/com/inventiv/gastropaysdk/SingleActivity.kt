package com.inventiv.gastropaysdk

import android.app.Activity
import android.os.Bundle

class SingleActivity: Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_common_gastropay_sdk)
    }
}