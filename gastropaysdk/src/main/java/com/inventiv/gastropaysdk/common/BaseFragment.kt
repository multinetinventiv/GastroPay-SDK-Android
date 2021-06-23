package com.inventiv.gastropaysdk.common

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import com.inventiv.gastropaysdk.R
import com.inventiv.gastropaysdk.ui.MainActivity
import com.inventiv.gastropaysdk.utils.REFERENCE_UNDEFINED
import com.inventiv.gastropaysdk.view.GastroPaySdkToolbar

internal abstract class BaseFragment(layoutId: Int) : Fragment(layoutId) {

    fun getMainActivity(): MainActivity {
        return requireActivity() as MainActivity
    }

    fun getToolbar(): GastroPaySdkToolbar? {
        return getMainActivity().findViewById(R.id.toolbarGastroPaySdk)
    }

    fun getLogo(): AppCompatImageView? {
        return getMainActivity().findViewById(R.id.imageMainLogoGastroPaySdk)
    }

    fun showToolbar(showLogo: Boolean) {
        getToolbar()?.apply {
            visibility = VISIBLE
            if (showLogo) {
                setCenterImage(REFERENCE_UNDEFINED)
                setTitle(REFERENCE_UNDEFINED)
            }
        }
        getLogo()?.visibility = if (showLogo) {
            VISIBLE
        } else {
            GONE
        }

    }

    fun hideToolbar() {
        getToolbar()?.visibility = GONE
        getLogo()?.visibility = GONE
    }

    abstract fun initDynamicViewProperties()

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            initDynamicViewProperties()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDynamicViewProperties()
    }

}