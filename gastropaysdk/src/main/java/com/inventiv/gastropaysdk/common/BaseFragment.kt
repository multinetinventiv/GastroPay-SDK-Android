package com.inventiv.gastropaysdk.common

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import com.inventiv.gastropaysdk.ui.MainActivity
import com.inventiv.gastropaysdk.utils.REFERENCE_UNDEFINED
import com.inventiv.gastropaysdk.view.GastroPaySdkToolbar

internal abstract class BaseFragment(layoutId: Int) : Fragment(layoutId) {

    fun getMainActivity(): MainActivity {
        return requireActivity() as MainActivity
    }

    abstract fun prepareToolbar(toolbar: GastroPaySdkToolbar, logo: AppCompatImageView)

    abstract fun showBottomNavigation(): Boolean

    fun showToolbar(showLogo: Boolean, toolbar: GastroPaySdkToolbar, logo: AppCompatImageView) {
        toolbar.apply {
            visibility = VISIBLE
            if (showLogo) {
                setCenterImage(REFERENCE_UNDEFINED)
                setTitle(REFERENCE_UNDEFINED)
            }
        }
        logo.visibility = if (showLogo) {
            VISIBLE
        } else {
            GONE
        }

    }

    fun hideToolbar(toolbar: GastroPaySdkToolbar, logo: AppCompatImageView) {
        toolbar.visibility = GONE
        logo.visibility = GONE
    }

    open fun initDynamicViewProperties() {}

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