package com.inventiv.gastropaysdk.sample

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.inventiv.gastropaysdk.shared.GastroPaySdk
import com.inventiv.gastropaysdk.shared.GastroPaySdkListener
import com.inventiv.gastropaysdk.shared.Language

class SdkStartActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_INFOS = "extra_infos"
        const val TAG = "MultinetWalletActivity"

        fun newIntent(context: Context, infos: InfoModel) =
            Intent(context, SdkStartActivity::class.java).apply {
                putExtra(EXTRA_INFOS, infos)
            }
    }

    private val removeInfosButton: MaterialButton by lazy {
        findViewById(R.id.removeInfosButton)
    }

    private val startSdkButton: MaterialButton by lazy {
        findViewById(R.id.startSdkButton)
    }

    private lateinit var infoModel: InfoModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sdk_start)

        infoModel = intent.getParcelableExtra(EXTRA_INFOS)!!

        removeInfosButton.setOnClickListener {
            getSharedPref().edit().clear().apply()
            startActivity(Intent(this, EnvironmentActivity::class.java))
            finish()
        }

        startSdkButton.setOnClickListener {
            GastroPaySdk.init(
                application,
                infoModel.environment,
                Language.TR,
                object : GastroPaySdkListener {
                    override fun onInitialized(isInitialized: Boolean) {
                        super.onInitialized(isInitialized)
                        Log.d("isInitialized", isInitialized.toString())
                    }
                })
        }
    }
}