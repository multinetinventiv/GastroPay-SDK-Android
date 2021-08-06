package com.inventiv.gastropaysdk.ui.pay

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.camera.core.CameraSelector
import androidx.lifecycle.ViewModelProvider
import com.google.mlkit.vision.barcode.Barcode
import com.inventiv.gastropaysdk.R
import com.inventiv.gastropaysdk.common.BaseFragment
import com.inventiv.gastropaysdk.databinding.FragmentPayGastropaySdkBinding
import com.inventiv.gastropaysdk.repository.MainRepositoryImp
import com.inventiv.gastropaysdk.shared.GastroPaySdk
import com.inventiv.gastropaysdk.ui.MainViewModel
import com.inventiv.gastropaysdk.ui.MainViewModelFactory
import com.inventiv.gastropaysdk.utils.blankj.utilcode.constant.PermissionConstants
import com.inventiv.gastropaysdk.utils.blankj.utilcode.util.LogUtils
import com.inventiv.gastropaysdk.utils.blankj.utilcode.util.PermissionUtils
import com.inventiv.gastropaysdk.utils.delegate.viewBinding
import com.inventiv.gastropaysdk.utils.qrreader.QRCameraConfiguration
import com.inventiv.gastropaysdk.utils.qrreader.QRReaderFragment
import com.inventiv.gastropaysdk.utils.qrreader.QRReaderListener
import com.inventiv.gastropaysdk.view.GastroPaySdkToolbar

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

    private val qrCodeReader: QRReaderFragment by lazy {
        childFragmentManager.findFragmentById(R.id.qrCodeReaderFragment) as QRReaderFragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()

        qrCodeReader.setListener(object : QRReaderListener {

            override fun onRead(barcode: Barcode, barcodes: List<Barcode>) {
                binding.testTextView.text = barcode.displayValue
            }

            override fun onError(exception: Exception) {
                binding.testTextView.text = exception.toString()
            }
        })

        PermissionUtils.permission(PermissionConstants.CAMERA)
            .callback(object : PermissionUtils.FullCallback {
                override fun onGranted(granted: MutableList<String>) {
                    LogUtils.d("onGranted")
                    val config = QRCameraConfiguration(lensFacing = CameraSelector.LENS_FACING_BACK)
                    qrCodeReader.startCamera(viewLifecycleOwner, config)
                }

                override fun onDenied(
                    deniedForever: MutableList<String>,
                    denied: MutableList<String>
                ) {
                    LogUtils.d("onDenied")
                }
            }).request()
    }

    private fun setupToolbar() {
        binding.toolbar.apply {
            textTitleGastroPaySdk.text = getString(R.string.qr_scan_qrcode_gastropay_sdk)
            rootLayoutToolbarGastroPaySdk.setBackgroundColor(Color.TRANSPARENT)
            imageToolbarLeftGastroPaySdk.setImageResource(R.drawable.ic_close_gastropay_sdk)
            textTitleGastroPaySdk.visibility = View.VISIBLE
            layoutLeftGastroPaySdk.visibility = View.VISIBLE
            layoutLeftGastroPaySdk.setOnClickListener {
                sharedViewModel.onBackPressed()
            }
        }
    }
}
