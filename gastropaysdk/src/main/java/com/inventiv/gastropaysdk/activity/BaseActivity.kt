package com.inventiv.gastropaysdk.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.inventiv.gastropaysdk.shared.GastroPaySdk
import com.inventiv.gastropaysdk.utils.updateBaseContextLocale

internal abstract class BaseActivity : AppCompatActivity() {
    override fun attachBaseContext(newBase: Context?) {
        if (newBase == null) {
            super.attachBaseContext(newBase)
        } else {
            super.attachBaseContext(
                updateBaseContextLocale(
                    newBase,
                    GastroPaySdk.getComponent().language().id
                )
            )
        }
    }
}