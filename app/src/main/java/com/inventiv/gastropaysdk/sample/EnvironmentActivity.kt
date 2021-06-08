package com.inventiv.gastropaysdk.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.google.gson.Gson
import com.inventiv.gastropaysdk.shared.Environment

class EnvironmentActivity : AppCompatActivity() {

    private val buttonDev: MaterialButton by lazy {
        findViewById(R.id.devEnvButton)
    }
    private val buttonPilot: MaterialButton by lazy {
        findViewById(R.id.pilotEnvButton)
    }
    private val buttonTest: MaterialButton by lazy {
        findViewById(R.id.testEnvButton)
    }
    private val buttonProduction: MaterialButton by lazy {
        findViewById(R.id.productionEnvButton)
    }
    private val textViewVersion: MaterialTextView by lazy {
        findViewById(R.id.versionGastropaySdkTextView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_environment)

        textViewVersion.text = getVersionText()
        buttonDev.setOnClickListener {
            saveAndGo(Environment.DEV)
        }
        buttonPilot.setOnClickListener {
            saveAndGo(Environment.PILOT)
        }
        buttonTest.setOnClickListener {
            saveAndGo(Environment.TEST)
        }
        buttonProduction.setOnClickListener {
            saveAndGo(Environment.PRODUCTION)
        }

        val sharedPref = getSharedPref()
        val strInfos = sharedPref.getString(PREF_INFOS, String())
        if (!strInfos.isNullOrEmpty()) {
            val info = Gson().fromJson(strInfos, InfoModel::class.java)
            saveAndGo(info.environment)
        }
    }

    private fun saveAndGo(environment: Environment) {
        val infoModel = InfoModel(environment)
        val info = Gson().toJson(infoModel)
        getSharedPref().edit().putString(PREF_INFOS, info).apply()


        startActivity(SdkStartActivity.newIntent(this, infoModel))
        finish()
    }
}