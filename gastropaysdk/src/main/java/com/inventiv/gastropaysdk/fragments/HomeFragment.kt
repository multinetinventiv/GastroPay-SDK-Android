package com.inventiv.gastropaysdk.fragments

import android.os.Bundle
import android.view.View
import com.inventiv.gastropaysdk.R
import com.inventiv.gastropaysdk.databinding.FragmentHomeGastropaySdkBinding
import com.inventiv.gastropaysdk.utils.blankj.utilcode.constant.PermissionConstants
import com.inventiv.gastropaysdk.utils.blankj.utilcode.util.LogUtils
import com.inventiv.gastropaysdk.utils.blankj.utilcode.util.PermissionUtils
import com.inventiv.gastropaysdk.utils.viewBinding

internal class HomeFragment : BaseFragment(R.layout.fragment_home_gastropay_sdk) {

    private val binding by viewBinding(FragmentHomeGastropaySdkBinding::bind)

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
