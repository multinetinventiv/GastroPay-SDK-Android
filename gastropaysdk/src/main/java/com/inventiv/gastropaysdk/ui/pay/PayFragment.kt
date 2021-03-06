package com.inventiv.gastropaysdk.ui.pay

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.camera.core.CameraSelector
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.mlkit.vision.barcode.Barcode
import com.inventiv.gastropaysdk.R
import com.inventiv.gastropaysdk.common.BaseFragment
import com.inventiv.gastropaysdk.common.CommonInfoDialogFragment
import com.inventiv.gastropaysdk.data.Resource
import com.inventiv.gastropaysdk.databinding.FragmentPayGastropaySdkBinding
import com.inventiv.gastropaysdk.repository.MainRepositoryImp
import com.inventiv.gastropaysdk.repository.PaymentRepositoryImp
import com.inventiv.gastropaysdk.shared.GastroPaySdk
import com.inventiv.gastropaysdk.ui.MainViewModel
import com.inventiv.gastropaysdk.ui.MainViewModelFactory
import com.inventiv.gastropaysdk.ui.pay.validate.PayValidateFragment
import com.inventiv.gastropaysdk.utils.blankj.utilcode.constant.PermissionConstants
import com.inventiv.gastropaysdk.utils.blankj.utilcode.util.AppUtils.getAppPackageName
import com.inventiv.gastropaysdk.utils.blankj.utilcode.util.PermissionUtils
import com.inventiv.gastropaysdk.utils.delegate.viewBinding
import com.inventiv.gastropaysdk.utils.handleError
import com.inventiv.gastropaysdk.utils.qrreader.QRCameraConfiguration
import com.inventiv.gastropaysdk.utils.qrreader.QRReaderFragment
import com.inventiv.gastropaysdk.utils.qrreader.QRReaderListener
import com.inventiv.gastropaysdk.utils.showError
import com.inventiv.gastropaysdk.view.GastroPaySdkToolbar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

internal class PayFragment : BaseFragment(R.layout.fragment_pay_gastropay_sdk) {

    override fun prepareToolbar(toolbar: GastroPaySdkToolbar, logo: AppCompatImageView) {
        hideToolbar(toolbar, logo)
    }

    override fun showBottomNavigation() = false

    private val binding by viewBinding(FragmentPayGastropaySdkBinding::bind)

    private val sharedViewModel: MainViewModel by lazy {
        val viewModelFactory = MainViewModelFactory(
            MainRepositoryImp(GastroPaySdk.getComponent().gastroPayService)
        )
        ViewModelProvider(requireActivity(), viewModelFactory).get(MainViewModel::class.java)
    }

    private val viewModel: PayViewModel by lazy {
        val viewModelFactory = PayViewModelFactory(
            PaymentRepositoryImp(GastroPaySdk.getComponent().gastroPayService)
        )
        ViewModelProvider(this, viewModelFactory).get(PayViewModel::class.java)
    }

    private val qrCodeReader: QRReaderFragment by lazy {
        childFragmentManager.findFragmentById(R.id.qrCodeReaderFragment) as QRReaderFragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupObservers()

        qrCodeReader.setListener(object : QRReaderListener {

            override fun onRead(barcode: Barcode, barcodes: List<Barcode>) {
                barcode.displayValue?.apply {
                    if (viewModel.requestInProgress.not()) {
                        viewModel.requestInProgress = true
                        viewModel.provisionInformation(this)
                    }
                }
            }

            override fun onError(exception: Exception) {
                showError(getString(R.string.qr_scan_qrcode_reading_error))
            }
        })

        PermissionUtils.permission(PermissionConstants.CAMERA)
            .callback(object : PermissionUtils.FullCallback {
                override fun onGranted(granted: MutableList<String>) {
                    val config = QRCameraConfiguration(lensFacing = CameraSelector.LENS_FACING_BACK)
                    qrCodeReader.startCamera(viewLifecycleOwner, config)
                }

                override fun onDenied(
                    deniedForever: MutableList<String>,
                    denied: MutableList<String>
                ) {
                    if (deniedForever.isNotEmpty()) {
                        CommonInfoDialogFragment.newInstance(
                            R.drawable.ic_camera_permission_gastropay_sdk,
                            getString(R.string.qr_scan_popup_camera_permission_body_gastropay_sdk),
                            null,
                            getString(R.string.qr_scan_popup_camera_permission_action_button_gastropay_sdk),
                            this@PayFragment::commonDialogDismissed
                        ) {
                            val action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            startActivity(
                                Intent(
                                    action, Uri.parse("package:${getAppPackageName()}")
                                )
                            )
                        }.show(childFragmentManager, null)
                    } else {
                        sharedViewModel.onBackPressed()
                    }
                }
            }).request()
    }

    private fun setupToolbar() {
        binding.toolbar.apply {
            textTitleGastroPaySdk.text = getString(R.string.qr_scan_qrcode_gastropay_sdk)
            rootLayoutToolbarGastroPaySdk.setBackgroundColor(Color.TRANSPARENT)
            imageToolbarLeftGastroPaySdk.setImageResource(R.drawable.ic_close_gastropay_sdk)
            textTitleGastroPaySdk.visibility = View.VISIBLE
            textTitleGastroPaySdk.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.white_gastropay_sdk
                )
            )
            layoutLeftGastroPaySdk.visibility = View.VISIBLE
            layoutLeftGastroPaySdk.setOnClickListener {
                sharedViewModel.onBackPressed()
            }
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            launch {
                viewModel.provisionInformationState.collect { uiState ->
                    when (uiState) {
                        is Resource.Loading -> {
                            if (uiState.isLoading) {
                                binding.loading.loadingLayout.visibility = View.VISIBLE
                            } else {
                                binding.loading.loadingLayout.visibility = View.GONE
                            }
                        }
                        is Resource.Success -> {
                            viewModel.requestInProgress = false
                            sharedViewModel.pushFragment(
                                PayValidateFragment.newInstance(
                                    toolbarTitle = uiState.data.merchantName,
                                    data = uiState.data
                                )
                            )
                        }
                        is Resource.Error -> {
                            viewModel.requestInProgress = false
                            uiState.apiError.handleError(requireActivity())
                        }
                        else -> {
                        }
                    }
                }
            }
        }
    }

    private fun commonDialogDismissed() {
        sharedViewModel.onBackPressed()
    }
}
