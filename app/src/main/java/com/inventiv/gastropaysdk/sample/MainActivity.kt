package com.inventiv.gastropaysdk.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.inventiv.gastropaysdk.shared.Environment
import com.inventiv.gastropaysdk.shared.GastroPaySdk
import com.inventiv.gastropaysdk.shared.GastroPaySdkListener
import com.inventiv.gastropaysdk.shared.Language

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        GastroPaySdk.init(application, Environment.TEST, Language.TR, object : GastroPaySdkListener{
            override fun onInitialized(isInitialized: Boolean) {
                super.onInitialized(isInitialized)
                Log.d("isInitialized", isInitialized.toString())
            }
        })

    }
}