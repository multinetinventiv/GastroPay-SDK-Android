package com.inventiv.gastropaysdk.ui.merchants

import android.Manifest.permission
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.inventiv.gastropaysdk.R
import com.inventiv.gastropaysdk.common.BaseFragment
import com.inventiv.gastropaysdk.data.model.Resource
import com.inventiv.gastropaysdk.data.model.response.Merchant
import com.inventiv.gastropaysdk.databinding.FragmentMerchantsGastropaySdkBinding
import com.inventiv.gastropaysdk.repository.MerchantRepositoryImp
import com.inventiv.gastropaysdk.shared.GastroPaySdk
import com.inventiv.gastropaysdk.ui.merchants.detail.MerchantDetailFragment
import com.inventiv.gastropaysdk.utils.CustomLoadingListItemCreator
import com.inventiv.gastropaysdk.utils.LocationHelper
import com.inventiv.gastropaysdk.utils.RecyclerMarginDecoration
import com.inventiv.gastropaysdk.utils.blankj.utilcode.util.ConvertUtils
import com.inventiv.gastropaysdk.utils.blankj.utilcode.util.LogUtils
import com.inventiv.gastropaysdk.utils.blankj.utilcode.util.PermissionUtils
import com.inventiv.gastropaysdk.utils.delegate.viewBinding
import com.inventiv.gastropaysdk.view.GastroPaySdkToolbar
import com.paginate.Paginate
import kotlinx.coroutines.flow.collect


internal class MerchantsFragment : BaseFragment(R.layout.fragment_merchants_gastropay_sdk) {

    private val binding by viewBinding(FragmentMerchantsGastropaySdkBinding::bind)

    private val locationHelper: LocationHelper by lazy {
        LocationHelper(
            activity = requireActivity(),
            fragment = this,
            globalLocationCallback = object : LocationHelper.GlobalLocationCallback {
                override fun onLocationResult(location: Location) {
                    myLocation = location
                    if (viewModel.currentPage == 0) {
                        viewModel.getMerchants(myLocation)
                    }
                }

                override fun onLocationSuccess() {
                    binding.layoutLocationPermissionGastroPaySdk.root.visibility = View.GONE
                }

                override fun onLocationFailed() {
                    LogUtils.e("onLocationFailed")
                }
            }
        )
    }
    private lateinit var merchantAdapter: MerchantAdapter
    private var merchantPaginate: Paginate? = null
    private var merchantsList = ArrayList<Merchant>()
    private var isLoadingPaging = false
    private var allItemsLoaded = false
    private var myLocation = Location("")
    private val paginateMerchantsCallbacks = object : Paginate.Callbacks {
        override fun onLoadMore() {
            if (viewModel.currentPage != 0) {
                isLoadingPaging = true
                viewModel.getMerchants(myLocation)
            }
        }

        override fun isLoading(): Boolean {
            return isLoadingPaging
        }

        override fun hasLoadedAllItems(): Boolean {
            return allItemsLoaded
        }
    }

    private val viewModel: MerchantsViewModel by lazy {
        val viewModelFactory = MerchantsViewModelFactory(
            MerchantRepositoryImp(GastroPaySdk.getComponent().gastroPayService)
        )
        ViewModelProvider(this, viewModelFactory).get(MerchantsViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
        setupClickListeners()
        setupMerchantAdapter()

        askLocationPermission()
    }

    private fun askLocationPermission() {
        if (PermissionUtils.isGranted(
                permission.ACCESS_FINE_LOCATION,
                permission.ACCESS_COARSE_LOCATION
            )
        ) {
            locationHelper.requestLocation()
        }
    }

    private fun setupMerchantAdapter() {
        merchantAdapter = MerchantAdapter(merchantsList) { merchant ->
            getMainActivity().pushFragment(
                MerchantDetailFragment.newInstance(merchant.merchantId)
            )
        }
        binding.merchantsRecyclerViewGastroPaySdk.addItemDecoration(
            RecyclerMarginDecoration(
                ConvertUtils.dp2px(
                    16f
                )
            )
        )
        binding.merchantsRecyclerViewGastroPaySdk.adapter = merchantAdapter
        merchantPaginate =
            Paginate.with(binding.merchantsRecyclerViewGastroPaySdk, paginateMerchantsCallbacks)
                .setLoadingTriggerThreshold(1)
                .addLoadingListItem(true)
                .setLoadingListItemCreator(CustomLoadingListItemCreator())
                .build()
    }

    override fun prepareToolbar(toolbar: GastroPaySdkToolbar, logo: AppCompatImageView) {
        showToolbar(true, toolbar, logo)
        toolbar.onRightIconClick {
            getMainActivity().closeSdk()
        }
    }

    override fun showBottomNavigation() = true

    private fun setupObservers() {
        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect { uiState ->
                when (uiState) {
                    is Resource.Loading -> {
                    }
                    is Resource.Success -> {
                        isLoadingPaging = false
                        if (uiState.data.isLastPage) {
                            allItemsLoaded = true
                        }
                        viewModel.currentPage = uiState.data.pageIndex + 1
                        if (uiState.data.merchants.isNullOrEmpty()) {
                            binding.emptyLayoutGastroPaySdk.visibility = View.VISIBLE
                        } else {
                            binding.emptyLayoutGastroPaySdk.visibility = View.GONE
                            merchantsList.addAll(uiState.data.merchants)
                        }
                        merchantAdapter.notifyDataSetChanged()
                    }
                    is Resource.Error -> {
                        LogUtils.e("Error", uiState.apiError)
                    }
                    else -> {
                    }
                }
            }
        }
    }

    private fun resetPaginate() {
        viewModel.currentPage = 0
        isLoadingPaging = false
        allItemsLoaded = false
    }

    private fun setupClickListeners() {
        binding.layoutLocationPermissionGastroPaySdk.root.setOnClickListener {
            locationHelper.requestLocation()
        }
    }

    override fun onDestroyView() {
        merchantPaginate?.unbind()
        super.onDestroyView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        locationHelper.onActivityResult(requestCode, resultCode, data)
    }
}