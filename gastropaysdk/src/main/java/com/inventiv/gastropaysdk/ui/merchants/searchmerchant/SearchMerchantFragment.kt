package com.inventiv.gastropaysdk.ui.merchants.searchmerchant

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.inventiv.gastropaysdk.R
import com.inventiv.gastropaysdk.common.BaseFragment
import com.inventiv.gastropaysdk.databinding.FragmentSearchMerchantGastropaySdkBinding
import com.inventiv.gastropaysdk.repository.MainRepositoryImp
import com.inventiv.gastropaysdk.repository.MerchantRepositoryImp
import com.inventiv.gastropaysdk.shared.GastroPaySdk
import com.inventiv.gastropaysdk.ui.MainViewModel
import com.inventiv.gastropaysdk.ui.MainViewModelFactory
import com.inventiv.gastropaysdk.utils.delegate.viewBinding
import com.inventiv.gastropaysdk.view.GastroPaySdkToolbar

internal class SearchMerchantFragment :
    BaseFragment(R.layout.fragment_search_merchant_gastropay_sdk) {

    companion object {
        fun newInstance() = SearchMerchantFragment()
    }

    private val binding by viewBinding(FragmentSearchMerchantGastropaySdkBinding::bind)

    private val viewModel: SearchMerchantViewModel by lazy {
        val viewModelFactory = SearchMerchantViewModelFactory(
            MerchantRepositoryImp(GastroPaySdk.getComponent().gastroPayService)
        )
        ViewModelProvider(this, viewModelFactory).get(SearchMerchantViewModel::class.java)
    }
    private val sharedViewModel: MainViewModel by lazy {
        val viewModelFactory = MainViewModelFactory(
            MainRepositoryImp(GastroPaySdk.getComponent().gastroPayService)
        )
        ViewModelProvider(requireActivity(), viewModelFactory).get(MainViewModel::class.java)
    }

    private lateinit var searchView: SearchView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSearchToolbar()
        setListeners()
        setupObservers()

//        viewModel.getMerchantDetail(merchantId)
    }

    private fun setupSearchToolbar() {
        binding.toolbarSearchMerchant.navigationIcon = null
        binding.toolbarSearchMerchant.inflateMenu(R.menu.search_merchant_menu_gastropay_sdk)
        searchView = binding.toolbarSearchMerchant.menu.findItem(
            R.id.action_search_restaurant_gastropay_sdk
        ).actionView as SearchView
        searchView.setIconifiedByDefault(false)
        searchView.maxWidth = Int.MAX_VALUE
        searchView.queryHint = resources.getString(R.string.search_merchant_searchview_hint_text)
        searchView.findViewById<View>(androidx.appcompat.R.id.search_plate).setBackgroundColor(
            ContextCompat.getColor(
                requireActivity(),
                android.R.color.transparent
            )
        )
    }

    private fun setListeners() {
        binding.apply {
            // TODO : setup listeners
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            /*viewModel.uiState.collect { uiState ->
                when (uiState) {
                    is Resource.Loading -> {
                        if (uiState.isLoading) {
                            binding.loadingLayout.visibility = View.VISIBLE
                        } else {
                            binding.loadingLayout.visibility = View.GONE
                        }
                    }
                    is Resource.Success -> {
                        uiState.data.apply {
                            Glide.with(this@SearchMerchantFragment)
                                .load(showcaseImageUrl)
                                .into(binding.merchantDetailImageView)
                            Glide.with(this@SearchMerchantFragment)
                                .load(logoUrl)
                                .into(binding.merchantImageView)
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
            }*/
        }
    }

    /*private fun AppCompatTextView.setEarnOrSpend(isBonusPoint: Boolean) {
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
    }*/

    /*private fun AppCompatTextView.setPhoneNumber(phoneNumber: String?, gsmNumber: String?) {
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
    }*/


    /*private fun String?.setDescription() {
        if (this.isNullOrEmpty()) {
            binding.descTitleTextView.visibility = View.GONE
            binding.descTextView.visibility = View.GONE
        } else {
            binding.descTextView.markdownText(this)
            binding.descTitleTextView.visibility = View.VISIBLE
            binding.descTextView.visibility = View.VISIBLE
        }
    }*/

    override fun prepareToolbar(toolbar: GastroPaySdkToolbar, logo: AppCompatImageView) {
        hideToolbar(toolbar, logo)
    }

    override fun showBottomNavigation() = false
}