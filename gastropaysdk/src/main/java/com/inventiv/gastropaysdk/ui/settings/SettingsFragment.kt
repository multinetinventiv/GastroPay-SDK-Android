package com.inventiv.gastropaysdk.ui.settings

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.inventiv.gastropaysdk.R
import com.inventiv.gastropaysdk.common.BaseFragment
import com.inventiv.gastropaysdk.data.Resource
import com.inventiv.gastropaysdk.databinding.FragmentSettingsGastropaySdkBinding
import com.inventiv.gastropaysdk.repository.MainRepositoryImp
import com.inventiv.gastropaysdk.repository.SettingsRepositoryImp
import com.inventiv.gastropaysdk.shared.GastroPaySdk
import com.inventiv.gastropaysdk.ui.MainViewModel
import com.inventiv.gastropaysdk.ui.MainViewModelFactory
import com.inventiv.gastropaysdk.ui.contactus.ContactUsFragment
import com.inventiv.gastropaysdk.ui.webview.WebViewFragment
import com.inventiv.gastropaysdk.utils.blankj.utilcode.util.LogUtils
import com.inventiv.gastropaysdk.utils.blankj.utilcode.util.StringUtils
import com.inventiv.gastropaysdk.utils.delegate.viewBinding
import com.inventiv.gastropaysdk.utils.getSettings
import com.inventiv.gastropaysdk.utils.handleError
import com.inventiv.gastropaysdk.view.GastroPaySdkToolbar
import com.multinetinventiv.gastropay.ui.profile.settings.notification.NotificationPreferencesFragment
import kotlinx.coroutines.flow.collect

internal class SettingsFragment : BaseFragment(R.layout.fragment_settings_gastropay_sdk) {

    override fun prepareToolbar(toolbar: GastroPaySdkToolbar, logo: AppCompatImageView) {
        hideToolbar(toolbar, logo)
    }

    override fun showBottomNavigation() = false

    private val binding by viewBinding(FragmentSettingsGastropaySdkBinding::bind)

    private val viewModel: SettingsViewModel by lazy {
        val viewModelFactory = SettingsViewModelFactory(
            SettingsRepositoryImp(GastroPaySdk.getComponent().gastroPayService)
        )
        ViewModelProvider(this, viewModelFactory).get(SettingsViewModel::class.java)
    }

    private val sharedViewModel: MainViewModel by lazy {
        val viewModelFactory = MainViewModelFactory(
            MainRepositoryImp(GastroPaySdk.getComponent().gastroPayService)
        )
        ViewModelProvider(requireActivity(), viewModelFactory).get(MainViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
        setupListeners()
    }

    private fun setupListeners() {
        binding.toolbar.setNavigationOnClickListener {
            sharedViewModel.onBackPressed()
        }
        binding.termsButton.setOnClickListener {
            viewModel.getTerms()
        }
        binding.permissionsButton.setOnClickListener {
            sharedViewModel.pushFragment(NotificationPreferencesFragment())

        }
        binding.contactUsButton.setOnClickListener {
            sharedViewModel.pushFragment(ContactUsFragment())
        }
        binding.faqButton.setOnClickListener{
            getSettings().faq?.let { url ->
                sharedViewModel.pushFragment(
                    WebViewFragment.newInstance(
                        toolbarTitle = StringUtils.getString(R.string.settings_faq_gastropay_sdk),
                        url = url
                    )
                )
            }
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.termsState.collect { uiState ->
                when (uiState) {
                    is Resource.Loading -> {
                        if (uiState.isLoading) {
                            binding.loading.loadingLayout.visibility = View.VISIBLE
                        } else {
                            binding.loading.loadingLayout.visibility = View.GONE
                        }
                    }
                    is Resource.Success -> {
                        uiState.data.apply {
                            sharedViewModel.pushFragment(
                                WebViewFragment.newInstance(
                                    toolbarTitle = StringUtils.getString(R.string.settings_terms_of_use_gastropay_sdk),
                                    url = url
                                )
                            )
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

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            sharedViewModel.coldEvent.collect {
                when (it) {
                    is MainViewModel.ColdEvent.OnGenericWebViewClick -> {
                        sharedViewModel.resetColdEvent()
                        LogUtils.d("GENERIC_WEBVIEW_IS_ACCEPT", it.isAccept)
                    }
                    else -> {
                    }
                }
            }
        }
    }

}