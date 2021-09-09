package com.inventiv.gastropaysdk.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.google.gson.Gson
import com.inventiv.gastropaysdk.shared.Environment

class EnvironmentActivity : AppCompatActivity(), OnCredentialsEntered {

    var selectedEnvironment: Environment? = null

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
            selectedEnvironment = Environment.DEV
            openCredentialDialog()
        }
        buttonPilot.setOnClickListener {
            selectedEnvironment = Environment.PILOT
            openCredentialDialog()
        }
        buttonTest.setOnClickListener {
            selectedEnvironment = Environment.TEST
            openCredentialDialog()
        }
        buttonProduction.setOnClickListener {
            selectedEnvironment = Environment.PRODUCTION
            openCredentialDialog()
        }

        val sharedPref = getSharedPref()
        val strInfos = sharedPref.getString(PREF_INFOS, String())
        if (!strInfos.isNullOrEmpty()) {
            val info = Gson().fromJson(strInfos, InfoModel::class.java)
            saveAndGo(info)
        }
    }

    private fun openCredentialDialog() {
        CredentialsDialogFragment.newInstance(selectedEnvironment!!).show(
            supportFragmentManager,
            CredentialsDialogFragment::class.java.simpleName
        )
    }

    private fun saveAndGo(infoModel: InfoModel) {
        val info = Gson().toJson(infoModel)
        getSharedPref().edit().putString(PREF_INFOS, info).apply()

        startActivity(SdkStartActivity.newIntent(this, infoModel))
        finish()
    }

    override fun onCredentialsEntered(infoModel: InfoModel) {
        saveAndGo(infoModel)
    }
}