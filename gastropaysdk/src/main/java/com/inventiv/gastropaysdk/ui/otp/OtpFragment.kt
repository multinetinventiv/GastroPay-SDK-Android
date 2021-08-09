package com.inventiv.gastropaysdk.ui.otp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.inventiv.gastropaysdk.R
import com.inventiv.gastropaysdk.common.BaseFragment
import com.inventiv.gastropaysdk.data.GastroPayUser
import com.inventiv.gastropaysdk.data.Resource
import com.inventiv.gastropaysdk.data.response.AuthenticationResponse
import com.inventiv.gastropaysdk.databinding.FragmentOtpGastropaySdkBinding
import com.inventiv.gastropaysdk.repository.AuthenticationRepositoryImp
import com.inventiv.gastropaysdk.repository.MainRepositoryImp
import com.inventiv.gastropaysdk.shared.GastroPaySdk
import com.inventiv.gastropaysdk.ui.MainViewModel
import com.inventiv.gastropaysdk.ui.MainViewModelFactory
import com.inventiv.gastropaysdk.utils.*
import com.inventiv.gastropaysdk.utils.blankj.utilcode.util.KeyboardUtils
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
        const val PARAM_FROM = "PARAM_FROM"

        fun newInstance(
            phoneNumber: String,
            verificationCode: String,
            endTime: String,
            from: NavigatedScreenType
        ) =
            OtpFragment().apply {
                val args = Bundle().apply {
                    putString(PARAM_PHONE_NUMBER, phoneNumber)
                    putString(PARAM_VERIFICATION_CODE, verificationCode)
                    putString(PARAM_END_TIME, endTime)
                    putInt(PARAM_FROM, from.id)
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
    private val sharedViewModel: MainViewModel by lazy {
        val viewModelFactory = MainViewModelFactory(
            MainRepositoryImp(GastroPaySdk.getComponent().gastroPayService)
        )
        ViewModelProvider(requireActivity(), viewModelFactory).get(MainViewModel::class.java)
    }

    private var simpleTextWatcher: TextWatcher = object : SimpleTextWatcher {
        override fun afterTextChanged(s: Editable?) {
            val otpCode = s.toString()
            val otpLength = otpCode.length
            if (otpLength == OTP_LENGHT) {
                when (from) {
                    NavigatedScreenType.LOGIN.id -> {
                        viewModel.otpConfirm(smsCode = otpCode, verificationCode = verificationCode)
                    }
                }
            }
        }
    }

    private var phoneNumber: String = ""
    private var verificationCode: String = ""
    private var endTime: Long = 0L
    private var from: Int = 0

    override fun prepareToolbar(toolbar: GastroPaySdkToolbar, logo: AppCompatImageView) {
        toolbar.apply {
            changeToLoginStyle()
            setTitle(R.string.login_toolbar_title_gastropay_sdk, R.color.celtic_gastropay_sdk)
            onLeftIconClick {
                sharedViewModel.onBackPressed()
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
            from = getInt(PARAM_FROM, 0)
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
            when (from) {
                NavigatedScreenType.LOGIN.id -> {
                    viewModel.resendCode(phoneNumber = phoneNumber)
                }
            }
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            launch {
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
                            KeyboardUtils.hideSoftInput(binding.pinEntryEditText)
                            when (from) {
                                NavigatedScreenType.LOGIN.id -> {
                                    loginToOtpSuccess(uiState.data)
                                }
                            }
                        }
                        is Resource.Error -> {
                            uiState.apiError.handleError(requireActivity())
                        }
                        else -> {
                        }
                    }
                }
            }

            launch {
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
                            verificationCode = uiState.data.verificationCode
                            endTime = uiState.data.endTime.toLong()
                            setupTimer()
                        }
                        is Resource.Error -> {
                            uiState.apiError.handleError(requireActivity())
                        }
                        else -> {
                        }
                    }
                }
            }
        }
    }

    private fun setupTimer() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
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

    private fun loginToOtpSuccess(data: AuthenticationResponse) {
        if (!data.refreshToken.isNullOrEmpty() &&
            !data.userToken.isNullOrEmpty()
        ) {
            GastroPaySdk.getComponent().apply {
                globalGastroPaySdkListener?.onAuthTokenReceived(data.userToken!!)
                isUserLoggedIn = true
                GastroPayUser.authToken = data.userToken
            }
            sharedViewModel.initTab(FragNavController.TAB1)
        }
    }
}