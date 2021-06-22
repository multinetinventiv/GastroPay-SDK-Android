package com.inventiv.gastropaysdk.ui.merchants.detail

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.chip.Chip
import com.inventiv.gastropaysdk.R
import com.inventiv.gastropaysdk.common.BaseFragment
import com.inventiv.gastropaysdk.data.model.Resource
import com.inventiv.gastropaysdk.data.model.response.Address
import com.inventiv.gastropaysdk.databinding.FragmentMerchantDetailGastropaySdkBinding
import com.inventiv.gastropaysdk.repository.MerchantRepositoryImp
import com.inventiv.gastropaysdk.shared.GastroPaySdk
import com.inventiv.gastropaysdk.utils.blankj.utilcode.util.LogUtils
import com.inventiv.gastropaysdk.utils.delegate.viewBinding
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

    private val viewModel: MerchantDetailViewModel by lazy {
        val viewModelFactory = MerchantDetailViewModelFactory(
            MerchantRepositoryImp(GastroPaySdk.getComponent().gastroPayService)
        )
        ViewModelProvider(this, viewModelFactory).get(MerchantDetailViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        merchantId = requireArguments().getString(PARAM_MERCHANT_ID)!!

        setListeners()
        setupObservers()

        viewModel.getMerchantDetail(merchantId)
    }

    override fun initDynamicViewProperties() {
        hideToolbar()
    }

    private fun setListeners() {
        binding.toolbar.setNavigationOnClickListener {
            getMainActivity().onBackPressed()
        }
        binding.appBarLayout.addOnOffsetChangedListener(getOffsetChangedListener())
    }

    private fun setupObservers() {
        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect { uiState ->
                when (uiState) {
                    is Resource.Loading -> {
                        LogUtils.d("Loading", uiState.isLoading)
                    }
                    is Resource.Success -> {
                        LogUtils.d("Success", uiState.data)

                        uiState.data.apply {
                            Glide.with(this@MerchantDetailFragment)
                                .load(showcaseImageUrl)
                                .into(binding.merchantDetailImageView)
                            Glide.with(this@MerchantDetailFragment)
                                .load(logoUrl)
                                .circleCrop()
                                .into(binding.merchantImageView)
                            toolbarTitle = name
                            binding.merchantNameTextView.text = name
                            binding.merchantAddressTextView.setAddress(address)
                            binding.merchantPhoneTextView.text =
                                if (!phoneNumber.isNullOrEmpty()) phoneNumber else gsmNumber
                            tags.forEach {
                                addChipToGroup(it.tagName)
                            }
                            if (note.isNullOrEmpty().not()) {
                                binding.descTextView.text = note
                                binding.descTitleTextView.visibility = View.VISIBLE
                                binding.descTextView.visibility = View.VISIBLE
                            } else {
                                binding.descTitleTextView.visibility = View.GONE
                                binding.descTextView.visibility = View.GONE
                            }
                            binding.expensivenessLayout.apply {
                                when {
                                    rate() >= 1 -> {
                                        expensivenessCheckBox1.isChecked = true
                                    }
                                    rate() >= 2 -> {
                                        expensivenessCheckBox2.isChecked = true
                                    }
                                    rate() >= 3 -> {
                                        expensivenessCheckBox3.isChecked = true
                                    }
                                }
                            }

                        }
                    }
                    is Resource.Error -> {
                        LogUtils.d("Error", uiState.apiError)
                    }
                    else -> {
                    }
                }
            }
        }
    }

    private fun addChipToGroup(title: String) {
        val chip = Chip(context).apply {
            text = title
            isChipIconVisible = false
            isCloseIconVisible = false
            isClickable = false
            isCheckable = false
            setTextColor(Color.BLACK)
            setChipBackgroundColorResource(R.color.white_smoke_gastropay_sdk)
        }
        binding.merchantTagsChipGroup.addView(chip as View)
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
}