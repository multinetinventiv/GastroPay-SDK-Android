package com.inventiv.gastropaysdk.ui.merchants.detail

import android.annotation.SuppressLint
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.chip.Chip
import com.inventiv.gastropaysdk.R
import com.inventiv.gastropaysdk.common.BaseFragment
import com.inventiv.gastropaysdk.data.Resource
import com.inventiv.gastropaysdk.data.response.Address
import com.inventiv.gastropaysdk.data.response.Tag
import com.inventiv.gastropaysdk.databinding.FragmentMerchantDetailGastropaySdkBinding
import com.inventiv.gastropaysdk.databinding.LayoutExpensivenessGastropaySdkBinding
import com.inventiv.gastropaysdk.repository.MainRepositoryImp
import com.inventiv.gastropaysdk.repository.MerchantRepositoryImp
import com.inventiv.gastropaysdk.shared.GastroPaySdk
import com.inventiv.gastropaysdk.ui.MainViewModel
import com.inventiv.gastropaysdk.ui.MainViewModelFactory
import com.inventiv.gastropaysdk.utils.*
import com.inventiv.gastropaysdk.utils.delegate.viewBinding
import com.inventiv.gastropaysdk.view.GastroPaySdkToolbar
import kotlinx.coroutines.flow.collect

internal class MerchantDetailFragment :
    BaseFragment(R.layout.fragment_merchant_detail_gastropay_sdk) {

    private val PARAM_MERCHANT_ID = "PARAM_MERCHANT_ID"

    companion object {
        fun newInstance(id: String) = MerchantDetailFragment().apply {
            val args = Bundle().apply {
                putString(PARAM_MERCHANT_ID, id)
            }
            arguments = args
        }
    }

    private val binding by viewBinding(FragmentMerchantDetailGastropaySdkBinding::bind)
    private var merchantId = ""
    private var toolbarTitle = ""
    private var merchantLocation = Location("")

    private val viewModel: MerchantDetailViewModel by lazy {
        val viewModelFactory = MerchantDetailViewModelFactory(
            MerchantRepositoryImp(GastroPaySdk.getComponent().gastroPayService)
        )
        ViewModelProvider(this, viewModelFactory).get(MerchantDetailViewModel::class.java)
    }
    private val sharedViewModel: MainViewModel by lazy {
        val viewModelFactory = MainViewModelFactory(
            MainRepositoryImp(GastroPaySdk.getComponent().gastroPayService)
        )
        ViewModelProvider(requireActivity(), viewModelFactory).get(MainViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        merchantId = requireArguments().getString(PARAM_MERCHANT_ID)!!

        setListeners()
        setupObservers()

        viewModel.getMerchantDetail(merchantId)
    }

    private fun setListeners() {
        binding.apply {
            toolbar.setNavigationOnClickListener {
                sharedViewModel.onBackPressed()
            }
            appBarLayout.addOnOffsetChangedListener(getOffsetChangedListener())
            navigationButton.setOnClickListener {
                requireContext().openGoogleMap(
                    merchantLocation.latitude,
                    merchantLocation.longitude
                )
            }
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect { uiState ->
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
                            binding.merchantDetailImageView.loadImage(
                                this@MerchantDetailFragment,
                                showcaseImageUrl
                            )
                            binding.merchantImageView.loadImage(
                                this@MerchantDetailFragment,
                                logoUrl
                            )
                            toolbarTitle = name
                            merchantLocation.latitude = latitude
                            merchantLocation.longitude = longitude
                            binding.merchantNameTextView.text = name
                            binding.earnOrSpendTextView.setEarnOrSpend(isBonusPoint)
                            binding.merchantAddressTextView.setAddress(address)
                            binding.merchantPhoneTextView.setPhoneNumber(phoneNumber, gsmNumber)
                            tags.setTags()
                            note.setDescription()
                            binding.expensivenessLayout.setExpensivenessLayout(rate())
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
    }

    private fun AppCompatTextView.setEarnOrSpend(isBonusPoint: Boolean) {
        if (isBonusPoint) {
            this.text = getString(R.string.merchant_detail_earn_gastropay_sdk)
        } else {
            this.text = getString(R.string.merchant_detail_spend_gastropay_sdk)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun AppCompatTextView.setAddress(address: Address?) {
        if (address?.city != null && address.neighbourhood != null) {
            this.text = "${address.city}, ${address.neighbourhood}"
            this.visibility = View.VISIBLE
        } else {
            this.visibility = View.GONE
        }
    }

    private fun AppCompatTextView.setPhoneNumber(phoneNumber: String?, gsmNumber: String?) {
        var number = ""
        if (!phoneNumber.isNullOrEmpty()) {
            this.visibility = View.VISIBLE
            number = "0$phoneNumber"
            this.text = phoneNumber.formatPhoneNumber()
        } else if (!gsmNumber.isNullOrEmpty()) {
            this.visibility = View.VISIBLE
            number = "0$gsmNumber"
            this.text = gsmNumber.formatPhoneNumber()
        } else {
            this.visibility = View.GONE
        }

        binding.phoneNumberLinearLayout.setOnClickListener {
            requireContext().openPhoneDialer(number)
        }
    }

    private fun List<Tag>.setTags() {
        if (this.isEmpty()) {
            binding.merchantTagsChipGroup.visibility = View.GONE
        } else {
            binding.merchantTagsChipGroup.visibility = View.VISIBLE
            this.forEach {
                val chip = Chip(context).apply {
                    text = it.tagName
                    isChipIconVisible = false
                    isCloseIconVisible = false
                    isClickable = false
                    isCheckable = false
                    setTextColor(Color.BLACK)
                    setChipBackgroundColorResource(R.color.white_smoke_gastropay_sdk)
                }
                binding.merchantTagsChipGroup.addView(chip as View)
            }
        }
    }

    private fun String?.setDescription() {
        if (this.isNullOrEmpty()) {
            binding.descTitleTextView.visibility = View.GONE
            binding.descTextView.visibility = View.GONE
        } else {
            binding.descTextView.markdownText(this)
            binding.descTitleTextView.visibility = View.VISIBLE
            binding.descTextView.visibility = View.VISIBLE
        }
    }

    private fun LayoutExpensivenessGastropaySdkBinding.setExpensivenessLayout(rate: Int) {
        this.apply {
            when {
                rate >= 1 -> {
                    expensivenessCheckBox1.isChecked = true
                }
                rate >= 2 -> {
                    expensivenessCheckBox2.isChecked = true
                }
                rate >= 3 -> {
                    expensivenessCheckBox3.isChecked = true
                }
            }
        }
    }

    private fun getOffsetChangedListener(): AppBarLayout.OnOffsetChangedListener {
        return object : AppBarLayout.OnOffsetChangedListener {
            var isShow: Boolean = false
            var scrollRange = -1

            override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout!!.totalScrollRange
                }
                if (scrollRange + verticalOffset == 0) {
                    binding.toolbarTitleTextView.text = toolbarTitle
                    isShow = true
                } else if (isShow) {
                    binding.toolbarTitleTextView.text = ""
                    isShow = false
                }
            }
        }
    }

    override fun prepareToolbar(toolbar: GastroPaySdkToolbar, logo: AppCompatImageView) {
        hideToolbar(toolbar, logo)
    }

    override fun showBottomNavigation() = false
}