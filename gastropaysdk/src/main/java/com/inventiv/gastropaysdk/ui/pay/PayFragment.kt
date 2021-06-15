package com.inventiv.gastropaysdk.ui.pay

import android.os.Bundle
import android.view.View
import com.inventiv.gastropaysdk.R
import com.inventiv.gastropaysdk.common.BaseFragment
import com.inventiv.gastropaysdk.databinding.FragmentPayGastropaySdkBinding
import com.inventiv.gastropaysdk.utils.blankj.utilcode.constant.PermissionConstants
import com.inventiv.gastropaysdk.utils.blankj.utilcode.util.LogUtils
import com.inventiv.gastropaysdk.utils.blankj.utilcode.util.PermissionUtils
import com.inventiv.gastropaysdk.utils.delegate.viewBinding

internal class PayFragment : BaseFragment(R.layout.fragment_pay_gastropay_sdk) {

    private val binding by viewBinding(FragmentPayGastropaySdkBinding::bind)

    override fun onViewCreated(view: View, bundle: Bundle?) {
        super.onViewCreated(view, bundle)

        PermissionUtils.permission(PermissionConstants.CAMERA)
            .callback(object : PermissionUtils.FullCallback {
                override fun onGranted(granted: MutableList<String>) {
                    LogUtils.d("onGranted")
                }

                override fun onDenied(
                    deniedForever: MutableList<String>,
                    denied: MutableList<String>
                ) {
                    LogUtils.d("onDenied")
                }
            }).request()
    }
}