package com.inventiv.gastropaysdk.ui.otp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.inventiv.gastropaysdk.R
import com.inventiv.gastropaysdk.common.BaseFragment
import com.inventiv.gastropaysdk.data.GastroPayUser
import com.inventiv.gastropaysdk.databinding.FragmentOtpGastropaySdkBinding
import com.inventiv.gastropaysdk.model.Resource
import com.inventiv.gastropaysdk.repository.AuthenticationRepositoryImp
import com.inventiv.gastropaysdk.shared.GastroPaySdk
import com.inventiv.gastropaysdk.utils.*
import com.inventiv.gastropaysdk.utils.blankj.utilcode.util.KeyboardUtils
import com.inventiv.gastropaysdk.utils.blankj.utilcode.util.LogUtils
import com.inventiv.gastropaysdk.utils.delegate.viewBinding
import com.inventiv.gastropaysdk.view.GastroPaySdkToolbar
import com.ncapdevi.fragnav.FragNavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

internal class OtpFragment : BaseFragment(R.layout.fragment_otp_gastropay_sdk) {

    companion object {
        const val PARAM_PHONE_NUMBER = "PARAM_PHONE_NUMBER"
        const val PARAM_VERIFICATION_CODE = "PARAM_VERIFICATION_CODE"
        const val PARAM_END_TIME = "PARAM_END_TIME"

        fun newInstance(phoneNumber: String, verificationCode: String, endTime: String) =
            OtpFragment().apply {
                val args = Bundle().apply {
                    putString(PARAM_PHONE_NUMBER, phoneNumber)
                    putString(PARAM_VERIFICATION_CODE, verificationCode)
                    putString(PARAM_END_TIME, endTime)
                }
                arguments = args
            }
    }

    private val binding by viewBinding(FragmentOtpGastropaySdkBinding::bind)
    private val viewModel: OtpViewModel by lazy {
        val viewModelFactory = OtpViewModelFactory(
            AuthenticationRepositoryImp(GastroPaySdk.getComponent().gastroPayService)
        )
        ViewModelProvider(this, viewModelFactory).get(OtpViewModel::class.java)
    }
    private var simpleTextWatcher: TextWatcher = object : SimpleTextWatcher {
        override fun afterTextChanged(s: Editable?) {
            val otpCode = s.toString()
            val otpLength = otpCode.length
            if (otpLength == OTP_LENGHT) {
                viewModel.otpConfirm(smsCode = otpCode, verificationCode = verificationCode)
            }
        }
    }

    private var phoneNumber: String = ""
    private var verificationCode: String = ""
    private var endTime: Long = 0L

    override fun prepareToolbar(toolbar: GastroPaySdkToolbar, logo: AppCompatImageView) {
        toolbar.apply {
            changeToLoginStyle()
            setTitle(R.string.login_toolbar_title_gastropay_sdk, R.color.celtic_gastropay_sdk)
            onLeftIconClick {
                getMainActivity().onBackPressed()
            }
        }
        showToolbar(false, toolbar, logo)
    }

    override fun showBottomNavigation(): Boolean {
        return false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireArguments().apply {
            phoneNumber = getString(PARAM_PHONE_NUMBER, "")
            verificationCode = getString(PARAM_VERIFICATION_CODE, "")
            endTime = getString(PARAM_END_TIME, "0").toLong()
        }
        binding.infoTextView.text = String.format(
            getString(R.string.otp_sms_label_gastropay_sdk),
            phoneNumber.formatPhoneNumber()
        )
        setupTimer()
        setupObservers()
        setupListeners()
        KeyboardUtils.showSoftInput(binding.pinEntryEditText)
    }

    private fun setupListeners() {
        binding.pinEntryEditText.addTextChangedListener(simpleTextWatcher)
        binding.resendButton.setOnClickListener {
            viewModel.resendCode(phoneNumber = phoneNumber)
        }
    }

    private fun setupObservers() {
        lifecycleScope.launchWhenStarted {
            viewModel.otpState.collect { uiState ->
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
                        KeyboardUtils.hideSoftInput(binding.pinEntryEditText)
                        if (!uiState.data.refreshToken.isNullOrEmpty() &&
                            !uiState.data.userToken.isNullOrEmpty()
                        ) {
                            GastroPaySdk.getComponent().apply {
                                globalGastroPaySdkListener?.onAuthTokenReceived(uiState.data.userToken!!)
                                isUserLoggedIn = true
                                GastroPayUser.authToken = uiState.data.userToken
                            }
                            getMainActivity().initTab(FragNavController.TAB1)
                        }
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
        lifecycleScope.launchWhenStarted {
            viewModel.resendCodeState.collect { uiState ->
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
                        verificationCode = uiState.data.verificationCode
                        endTime = uiState.data.endTime.toLong()
                        setupTimer()
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

    private fun setupTimer() {
        viewModel.viewModelScope.launch {
            val totalSeconds = endTime.calculateDifferenceWithCurrentTime()
            val tickSeconds = 1
            for (second in totalSeconds downTo tickSeconds) {
                val time = String.format(
                    "%02d:%02d",
                    TimeUnit.SECONDS.toMinutes(second),
                    second - TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(second))
                )
                setTimerText(time)
                delay(1000)
            }
            setTimerText("00:00")
            binding.resendButton.isEnabled = true
        }
    }

    private fun setTimerText(text: String) {
        val formattedTimerText =
            String.format(getString(R.string.otp_timer_label_gastropay_sdk), text)
        binding.timerInfoTextView.text = formattedTimerText
    }
}