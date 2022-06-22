package com.inventiv.gastropaysdk.ui.webview

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.ViewModelProvider
import com.inventiv.gastropaysdk.R
import com.inventiv.gastropaysdk.common.BaseFragment
import com.inventiv.gastropaysdk.databinding.FragmentWebviewGastropaySdkBinding
import com.inventiv.gastropaysdk.repository.MainRepositoryImp
import com.inventiv.gastropaysdk.shared.GastroPaySdk
import com.inventiv.gastropaysdk.ui.MainViewModel
import com.inventiv.gastropaysdk.ui.MainViewModelFactory
import com.inventiv.gastropaysdk.utils.REFERENCE_UNDEFINED
import com.inventiv.gastropaysdk.utils.delegate.viewBinding
import com.inventiv.gastropaysdk.view.GastroPaySdkToolbar

internal class WebViewFragment : BaseFragment(R.layout.fragment_webview_gastropay_sdk) {

    companion object {
        private const val PARAM_TOOLBAR_TITLE = "PARAM_TOOLBAR_TITLE"
        private const val PARAM_URL = "PARAM_URL"
        private const val PARAM_SHOW_BOTTOM_BUTTONS = "PARAM_SHOW_BOTTOM_BUTTONS"

        fun newInstance(
            toolbarTitle: String,
            url: String,
            showBottomButtons: Boolean = false
        ) = WebViewFragment().apply {
            val args = Bundle().apply {
                putString(PARAM_TOOLBAR_TITLE, toolbarTitle)
                putString(PARAM_URL, url)
                putBoolean(PARAM_SHOW_BOTTOM_BUTTONS, showBottomButtons)
            }
            arguments = args
        }
    }

    override fun prepareToolbar(toolbar: GastroPaySdkToolbar, logo: AppCompatImageView) {
        val toolbarTitle = requireArguments().getString(PARAM_TOOLBAR_TITLE, "")
        toolbar.apply {
            changeToLoginStyle()
            setTitle(toolbarTitle, R.color.celtic_gastropay_sdk)
            setLeftIcon(R.drawable.ic_arrow_back_gastropay_sdk)
            setRightIcon(REFERENCE_UNDEFINED, null)
            onLeftIconClick {
                sharedViewModel.onBackPressed()
            }
        }
        showToolbar(false, toolbar, logo)
    }

    override fun showBottomNavigation() = false

    private val binding by viewBinding(FragmentWebviewGastropaySdkBinding::bind)
    private var webViewUrl = ""

    private val viewModel: WebViewViewModel by lazy {
        val viewModelFactory = WebViewViewModelFactory()
        ViewModelProvider(this, viewModelFactory).get(WebViewViewModel::class.java)
    }

    private val sharedViewModel: MainViewModel by lazy {
        val viewModelFactory = MainViewModelFactory(
            MainRepositoryImp(GastroPaySdk.getComponent().gastroPayService)
        )
        ViewModelProvider(requireActivity(), viewModelFactory).get(MainViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        webViewUrl = requireArguments().getString(PARAM_URL)!!
        if (requireArguments().getBoolean(PARAM_SHOW_BOTTOM_BUTTONS)) {
            binding.bottomButtons.visibility = View.VISIBLE
        }
        setupListeners()
        setupWebView()
    }

    private fun setupListeners() {
        binding.apply {
            cancelButton.setOnClickListener {
                sharedViewModel.onGenericWebViewClicked(isAccept = false)
            }
            acceptButton.setOnClickListener {
                sharedViewModel.onGenericWebViewClicked(isAccept = true)
            }
        }
    }

    private fun setupWebView() {
        binding.genericWebView.apply {
            settings.javaScriptEnabled = true
            webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    binding.loading.loadingLayout.visibility = View.VISIBLE
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    if (getView() != null) {
                        binding.loading.loadingLayout.visibility = View.GONE
                    }
                }
            }
            loadUrl(webViewUrl)
        }
    }
}