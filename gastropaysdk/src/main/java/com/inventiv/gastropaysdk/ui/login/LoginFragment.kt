package com.inventiv.gastropaysdk.ui.login

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.inventiv.gastropaysdk.R
import com.inventiv.gastropaysdk.common.BaseFragment
import com.inventiv.gastropaysdk.databinding.FragmentLoginGastropaySdkBinding
import com.inventiv.gastropaysdk.model.Resource
import com.inventiv.gastropaysdk.repository.AuthenticationRepositoryImp
import com.inventiv.gastropaysdk.shared.GastroPaySdk
import com.inventiv.gastropaysdk.ui.otp.OtpFragment
import com.inventiv.gastropaysdk.utils.*
import com.inventiv.gastropaysdk.utils.blankj.utilcode.util.KeyboardUtils
import com.inventiv.gastropaysdk.utils.blankj.utilcode.util.LogUtils
import com.inventiv.gastropaysdk.utils.delegate.viewBinding
import com.inventiv.gastropaysdk.view.GastroPaySdkToolbar
import com.ncapdevi.fragnav.FragNavController
import kotlinx.coroutines.flow.collect

internal class LoginFragment : BaseFragment(R.layout.fragment_login_gastropay_sdk) {

    private val binding by viewBinding(FragmentLoginGastropaySdkBinding::bind)
    private val viewModel: LoginViewModel by lazy {
        val viewModelFactory = LoginViewModelFactory(
            AuthenticationRepositoryImp(GastroPaySdk.getComponent().gastroPayService)
        )
        ViewModelProvider(this, viewModelFactory).get(LoginViewModel::class.java)
    }
    private val watcher: PhoneNumberTextWatcher by lazy {
        PhoneNumberTextWatcher(
            binding.phoneTextInputEditText,
            object : MaskWatcherView {
                override fun afterTextTriggered() {
                    super.afterTextTriggered()
                    val validPhone = binding.phoneTextInputEditText.text.toString().isValidPhone()
                    binding.loginButton.isEnabled = validPhone
                }
            }
        )
    }

    override fun prepareToolbar(toolbar: GastroPaySdkToolbar, logo: AppCompatImageView) {
        toolbar.apply {
            changeToLoginStyle()
            setTitle(R.string.login_toolbar_title_gastropay_sdk, R.color.celtic_gastropay_sdk)
            onLeftIconClick {
                //TODO fix after all done
                GastroPaySdk.getComponent().isUserLoggedIn = true
                getMainActivity().initTab(FragNavController.TAB1)
            }
        }
        showToolbar(false, toolbar, logo)
    }

    override fun showBottomNavigation(): Boolean {
        return false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
        setupListeners()
        binding.phoneTextInputEditText.requestFocus()
    }

    private fun setupListeners() {
        binding.phoneTextInputEditText.addTextChangedListener(watcher)
        binding.loginButton.setOnClickListener {
            val phoneNumber = binding.phoneTextInputEditText.text.toString().toServicePhoneNumber()
            viewModel.login(phoneNumber)
            KeyboardUtils.hideSoftInput(requireActivity())
        }
    }

    private fun setupObservers() {
        lifecycleScope.launchWhenStarted {
            viewModel.loginState.collect { uiState ->
                when (uiState) {
                    is Resource.Loading -> {
                        if (uiState.isLoading) {
                            binding.loadingLayout.visibility = View.VISIBLE
                        } else {
                            binding.loadingLayout.visibility = View.GONE
                        }
                    }
                    is Resource.Success -> {
                        LogUtils.d(uiState.data)
                        val phoneNumber =
                            binding.phoneTextInputEditText.text.toString().toServicePhoneNumber()
                        getMainActivity().pushFragment(
                            OtpFragment.newInstance(
                                verificationCode = uiState.data.verificationCode,
                                endTime = uiState.data.endTime,
                                phoneNumber = phoneNumber
                            )
                        )
                    }
                    is Resource.Error -> {
                        LogUtils.e("Error", uiState.apiError)
                        uiState.apiError.handleError(requireActivity())
                    }
                    else -> {
                    }
                }
            }
        }
    }
}