package com.inventiv.gastropaysdk.sample

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.inventiv.gastropaysdk.shared.GastroPaySdk
import com.inventiv.gastropaysdk.shared.GastroPaySdkException
import com.inventiv.gastropaysdk.shared.GastroPaySdkListener
import com.inventiv.gastropaysdk.shared.Language

class SdkStartActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_INFOS = "extra_infos"

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

    private val textViewVersion: MaterialTextView by lazy {
        findViewById(R.id.versionGastropaySdkTextView)
    }

    private lateinit var infoModel: InfoModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sdk_start)

        infoModel = intent.getParcelableExtra(EXTRA_INFOS)!!

        textViewVersion.text = getVersionText()
        try {
            GastroPaySdk.init(
                application = application,
                environment = infoModel.environment,
                obfuscationKey = infoModel.obfuscationKey,
                language = Language.TR,
                logging = true,
                listener = object : GastroPaySdkListener {
                    override fun onInitialized(isInitialized: Boolean) {
                        super.onInitialized(isInitialized)
                        Log.d("isInitialized", isInitialized.toString())
                    }

                    override fun onAuthTokenReceived(authToken: String) {
                        super.onAuthTokenReceived(authToken)
                        Log.d("onAuthTokenReceived", authToken)
                    }

                    override fun onSDKClosed() {
                        super.onSDKClosed()
                        Log.d("onSDKClosed", "sdk closed")
                    }
                })
        } catch (e: GastroPaySdkException) {
            // Sdk couldn't be loaded successfully, check private key if its correct
            Toast.makeText(
                this@SdkStartActivity,
                "GastroPaySdkError : ${e.message}",
                Toast.LENGTH_LONG
            ).show()
            removeInfoAndReturn()
        }

        removeInfosButton.setOnClickListener {
            removeInfoAndReturn()
        }

        startSdkButton.setOnClickListener {
            GastroPaySdk.start(
                context = this,
                authToken = null
            )
        }
    }

    private fun removeInfoAndReturn() {
        getSharedPref().edit().clear().apply()
        startActivity(Intent(this, EnvironmentActivity::class.java))
        finish()
    }
}