package com.inventiv.gastropaysdk.shared

import android.app.Application
import com.inventiv.gastropaysdk.BuildConfig
import com.inventiv.gastropaysdk.GastroPaySdkComponent
import com.inventiv.gastropaysdk.utils.blankj.utilcode.util.LogUtils
import com.inventiv.gastropaysdk.utils.blankj.utilcode.util.Utils

object GastroPaySdk {

    private lateinit var gastroPaySdkComponent: GastroPaySdkComponent

    internal fun getComponent() = gastroPaySdkComponent


    @JvmStatic
    fun setLanguage(language: Language) = getComponent().setLanguage(language)

    @JvmStatic
    fun setGlobalGastroPaySdkListener(gastroPaySdkListener: GastroPaySdkListener) {
        getComponent().globalGastroPaySdkListener = gastroPaySdkListener
    }

    @JvmOverloads
    @JvmStatic
    @Throws(GastroPaySdkException::class)
    fun init(
        application: Application,
        environment: Environment = Environment.PRODUCTION,
        language: Language? = null,
        listener: GastroPaySdkListener? = null,
    ) {
        Utils.init(application)
        LogUtils.getConfig().isLogSwitch = BuildConfig.DEBUG

        //TODO : privateKey değeri doğru gelmezse sdk çalışmıyor ve endpoint şifresi çözülmüyor olacak
        /*try {
            environment.baseUrl = AESHelper.decrypt(environment.encryptedBaseUrl, privateKey)!!
            environment.apiServicePath =
                AESHelper.decrypt(environment.encryptedApiServicePath, privateKey)!!
        } catch (exception: Exception) {
            throw GastroPaySdkException.SecurityException(application.getString(R.string.security_exception_message))
        }*/

        //TODO : test amaçlı burada aktivite açılıyor. ileride buradan kaldırılacak
        /*Intent(application, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            application.startActivity(this)
        }*/

        this.gastroPaySdkComponent = GastroPaySdkComponent(
            appContext = application,
            environment = environment,
            language = language
        ).apply {
            globalGastroPaySdkListener = listener
        }

        getComponent().globalGastroPaySdkListener?.onInitialized(true)
    }

}