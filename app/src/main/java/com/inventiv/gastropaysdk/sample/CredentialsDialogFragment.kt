package com.inventiv.gastropaysdk.sample

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.inventiv.gastropaysdk.shared.Environment

class CredentialsDialogFragment : BottomSheetDialogFragment() {

    companion object {
        private const val ARG_ENVIRONMENT = "arg_environment"

        fun newInstance(environment: Environment) = CredentialsDialogFragment().apply {
            val args = Bundle().apply {
                putSerializable(ARG_ENVIRONMENT, environment)
            }
            arguments = args
        }
    }

    private val buttonStartSdk: MaterialButton? by lazy {
        view?.findViewById(R.id.buttonStartSdk)
    }

    private val editTextObfuscationKey: TextInputEditText? by lazy {
        view?.findViewById(R.id.textInputEditObfuscationKey)
    }

    private lateinit var environment: Environment

    private lateinit var onCredentialsEntered: OnCredentialsEntered

    override fun onAttach(context: Context) {
        super.onAttach(context)
        onCredentialsEntered = context as OnCredentialsEntered
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        environment = arguments?.getSerializable(ARG_ENVIRONMENT) as Environment
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dialog_bottom_credentials, container, false)
    }

    override fun getTheme(): Int = R.style.Theme_DialogStyle

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        insertPreDefinedData()
        buttonStartSdk?.setOnClickListener {
            val obfuscationKey = editTextObfuscationKey?.text.toString()
            val infoModel = InfoModel(environment, obfuscationKey)
            onCredentialsEntered.onCredentialsEntered(infoModel)
        }
    }

    private fun insertPreDefinedData() {
        if (BuildConfig.OBFUSCATION_KEY.isNotEmpty()) {
            editTextObfuscationKey?.setText(BuildConfig.OBFUSCATION_KEY)
        }
    }
}

interface OnCredentialsEntered {
    fun onCredentialsEntered(infoModel: InfoModel)
}