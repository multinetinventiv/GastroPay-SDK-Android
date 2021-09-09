package com.inventiv.gastropaysdk.ui.pay.result

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.ViewModelProvider
import com.inventiv.gastropaysdk.R
import com.inventiv.gastropaysdk.common.BaseFragment
import com.inventiv.gastropaysdk.data.response.ProvisionInformationResponse
import com.inventiv.gastropaysdk.databinding.FragmentPayResultGastropaySdkBinding
import com.inventiv.gastropaysdk.repository.MainRepositoryImp
import com.inventiv.gastropaysdk.repository.PaymentRepositoryImp
import com.inventiv.gastropaysdk.shared.GastroPaySdk
import com.inventiv.gastropaysdk.ui.MainViewModel
import com.inventiv.gastropaysdk.ui.MainViewModelFactory
import com.inventiv.gastropaysdk.utils.REFERENCE_UNDEFINED
import com.inventiv.gastropaysdk.utils.blankj.utilcode.util.StringUtils
import com.inventiv.gastropaysdk.utils.delegate.viewBinding
import com.inventiv.gastropaysdk.view.GastroPaySdkToolbar
import com.ncapdevi.fragnav.FragNavController

internal class PayResultFragment : BaseFragment(R.layout.fragment_pay_result_gastropay_sdk) {

    companion object {
        private const val PARAM_TOOLBAR_TITLE = "PARAM_TOOLBAR_TITLE"
        private const val PARAM_PROVISION_DATA = "PARAM_PROVISION_DATA"

        fun newInstance(
            toolbarTitle: String?,
            data: ProvisionInformationResponse
        ) = PayResultFragment().apply {
            val args = Bundle().apply {
                putString(PARAM_TOOLBAR_TITLE, toolbarTitle)
                putParcelable(PARAM_PROVISION_DATA, data)
            }
            arguments = args
        }
    }

    override fun prepareToolbar(toolbar: GastroPaySdkToolbar, logo: AppCompatImageView) {
        val toolbarTitle = requireArguments().getString(
            PARAM_TOOLBAR_TITLE,
            StringUtils.getString(R.string.payment_success_navigation_title_gastropay_sdk)
        )
        toolbar.apply {
            changeToMainStyle()
            setTitle(toolbarTitle, R.color.white_gastropay_sdk)
            setLeftIcon(R.drawable.ic_close_gastropay_sdk)
            setRightIcon(REFERENCE_UNDEFINED, null)
            onLeftIconClick {
                closeFragment()
            }
        }
        showToolbar(false, toolbar, logo)
    }

    override fun showBottomNavigation() = false

    private val binding by viewBinding(FragmentPayResultGastropaySdkBinding::bind)
    private lateinit var provision: ProvisionInformationResponse

    private val sharedViewModel: MainViewModel by lazy {
        val viewModelFactory = MainViewModelFactory(
            MainRepositoryImp(GastroPaySdk.getComponent().gastroPayService)
        )
        ViewModelProvider(requireActivity(), viewModelFactory).get(MainViewModel::class.java)
    }

    private val viewModel: PayResultViewModel by lazy {
        val viewModelFactory = PayResultViewModelFactory(
            PaymentRepositoryImp(GastroPaySdk.getComponent().gastroPayService)
        )
        ViewModelProvider(this, viewModelFactory).get(PayResultViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        provision = requireArguments().getParcelable(PARAM_PROVISION_DATA)!!

        setupListeners()
        setupUI()
    }

    private fun setupListeners() {
        binding.endButton.setOnClickListener {
            closeFragment()
        }
    }

    private fun setupUI() {
        binding.amountTextView.text = provision.totalAmount.displayValue
    }

    private fun closeFragment(){
        sharedViewModel.initTab(FragNavController.TAB2)
    }
}