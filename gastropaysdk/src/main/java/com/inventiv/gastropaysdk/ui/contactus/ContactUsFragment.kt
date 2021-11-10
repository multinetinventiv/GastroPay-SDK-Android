package com.inventiv.gastropaysdk.ui.contactus

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.ViewModelProvider
import com.inventiv.gastropaysdk.R
import com.inventiv.gastropaysdk.common.BaseFragment
import com.inventiv.gastropaysdk.databinding.FragmentContactusGastropaySdkBinding
import com.inventiv.gastropaysdk.repository.MainRepositoryImp
import com.inventiv.gastropaysdk.shared.GastroPaySdk
import com.inventiv.gastropaysdk.ui.MainViewModel
import com.inventiv.gastropaysdk.ui.MainViewModelFactory
import com.inventiv.gastropaysdk.utils.delegate.viewBinding
import com.inventiv.gastropaysdk.utils.getSettings
import com.inventiv.gastropaysdk.view.GastroPaySdkToolbar

internal class ContactUsFragment : BaseFragment(R.layout.fragment_contactus_gastropay_sdk) {

    override fun prepareToolbar(toolbar: GastroPaySdkToolbar, logo: AppCompatImageView) {
        hideToolbar(toolbar, logo)
    }

    override fun showBottomNavigation() = false

    private val binding by viewBinding(FragmentContactusGastropaySdkBinding::bind)

    private val sharedViewModel: MainViewModel by lazy {
        val viewModelFactory = MainViewModelFactory(
            MainRepositoryImp(GastroPaySdk.getComponent().gastroPayService)
        )
        ViewModelProvider(requireActivity(), viewModelFactory).get(MainViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
    }

    private fun setupListeners() {
        binding.toolbar.setNavigationOnClickListener {
            sharedViewModel.onBackPressed()
        }
        binding.writeButton.setOnClickListener {
            val email = getSettings().email ?: getString(R.string.default_email_gastropay_sdk)
            val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:$email"))
            startActivity(Intent.createChooser(emailIntent, String()))
        }
    }
}