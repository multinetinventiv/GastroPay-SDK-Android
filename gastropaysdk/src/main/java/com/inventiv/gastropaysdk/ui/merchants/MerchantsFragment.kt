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
import com.inventiv.gastropaysdk.data.Resource
import com.inventiv.gastropaysdk.data.response.MerchantResponse
import com.inventiv.gastropaysdk.databinding.FragmentMerchantsGastropaySdkBinding
import com.inventiv.gastropaysdk.repository.MainRepositoryImp
import com.inventiv.gastropaysdk.repository.MerchantRepositoryImp
import com.inventiv.gastropaysdk.shared.GastroPaySdk
import com.inventiv.gastropaysdk.ui.MainViewModel
import com.inventiv.gastropaysdk.ui.MainViewModelFactory
import com.inventiv.gastropaysdk.ui.merchants.detail.MerchantDetailFragment
import com.inventiv.gastropaysdk.ui.merchants.searchmerchant.SearchMerchantFragment
import com.inventiv.gastropaysdk.utils.CustomLoadingListItemCreator
import com.inventiv.gastropaysdk.utils.LocationHelper
import com.inventiv.gastropaysdk.utils.blankj.utilcode.util.ConvertUtils
import com.inventiv.gastropaysdk.utils.blankj.utilcode.util.LogUtils
import com.inventiv.gastropaysdk.utils.blankj.utilcode.util.PermissionUtils
import com.inventiv.gastropaysdk.utils.delegate.viewBinding
import com.inventiv.gastropaysdk.utils.itemdecorator.RecyclerMarginDecoration
import com.inventiv.gastropaysdk.view.GastroPaySdkToolbar
import com.paginate.Paginate
import kotlinx.coroutines.flow.collect


internal class MerchantsFragment : BaseFragment(R.layout.fragment_merchants_gastropay_sdk) {

    private val binding by viewBinding(FragmentMerchantsGastropaySdkBinding::bind)

    private lateinit var merchantAdapter: MerchantAdapter
    private var merchantPaginate: Paginate? = null
    private var merchantsList = ArrayList<MerchantResponse>()
    private var isLoadingPaging = false
    private var allItemsLoaded = false
    private var myLocation = Location("")
    private var tags: String? = String()
    private var merchantName: String? = null
    private var cityId: String? = null

    private val viewModel: MerchantsViewModel by lazy {
        val viewModelFactory = MerchantsViewModelFactory(
            MerchantRepositoryImp(GastroPaySdk.getComponent().gastroPayService)
        )
        ViewModelProvider(this, viewModelFactory).get(MerchantsViewModel::class.java)
    }
    private val sharedViewModel: MainViewModel by lazy {
        val viewModelFactory = MainViewModelFactory(
            MainRepositoryImp(GastroPaySdk.getComponent().gastroPayService)
        )
        ViewModelProvider(requireActivity(), viewModelFactory).get(MainViewModel::class.java)
    }

    private lateinit var locationHelper: LocationHelper

    private val paginateMerchantsCallbacks = object : Paginate.Callbacks {
        override fun onLoadMore() {
            if (viewModel.currentPage != 0) {
                isLoadingPaging = true
                viewModel.getMerchants(
                    location = myLocation,
                    merchantName = merchantName,
                    tags = tags,
                    cityId = cityId
                )
            }
        }

        override fun isLoading(): Boolean {
            return isLoadingPaging
        }

        override fun hasLoadedAllItems(): Boolean {
            return allItemsLoaded
        }
    }

    override fun prepareToolbar(toolbar: GastroPaySdkToolbar, logo: AppCompatImageView) {
        toolbar.changeToMainStyle()
        showToolbar(true, toolbar, logo)
        toolbar.onRightIconClick {
            sharedViewModel.closeSdk()
        }
    }

    override fun showBottomNavigation() = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initLocationHelper()
        subscribeNavigationEvents()
        setupObservers()
        setupClickListeners()
        setupMerchantAdapter()

        askLocationPermission()
    }

    private fun initLocationHelper() {
        locationHelper = LocationHelper(
            activity = requireActivity(),
            fragment = this,
            lifecycle = viewLifecycleOwner.lifecycle,
            callback = object : LocationHelper.GlobalLocationCallback {
                override fun onLocationRequest() {
                    binding.layoutLocationPermissionGastroPaySdk.root.visibility = View.GONE
                    binding.loading.loadingLayout.visibility = View.VISIBLE
                }

                override fun onLocationResult(location: Location) {
                    binding.loading.loadingLayout.visibility = View.GONE
                    myLocation = location
                    if (viewModel.currentPage == 0) {
                        viewModel.getMerchants(
                            location = myLocation,
                            merchantName = merchantName,
                            tags = tags,
                            cityId = cityId
                        )
                    }
                }

                override fun onLocationFailed() {
                    binding.loading.loadingLayout.visibility = View.GONE
                    LogUtils.e("onLocationFailed")
                }
            }
        )
    }

    private fun askLocationPermission() {
        if (PermissionUtils.isGranted(
                permission.ACCESS_FINE_LOCATION,
                permission.ACCESS_COARSE_LOCATION
            )
        ) {
            locationHelper.requestLocation()
        } else {
            binding.layoutLocationPermissionGastroPaySdk.root.visibility = View.VISIBLE
        }
    }

    private fun setupMerchantAdapter() {
        merchantAdapter = MerchantAdapter(merchantsList) { merchant ->
            sharedViewModel.pushFragment(
                MerchantDetailFragment.newInstance(merchant.merchantId)
            )
        }
        binding.merchantsRecyclerViewGastroPaySdk.addItemDecoration(
            RecyclerMarginDecoration(ConvertUtils.dp2px(16f))
        )
        binding.merchantsRecyclerViewGastroPaySdk.adapter = merchantAdapter
        merchantPaginate =
            Paginate.with(binding.merchantsRecyclerViewGastroPaySdk, paginateMerchantsCallbacks)
                .setLoadingTriggerThreshold(1)
                .addLoadingListItem(true)
                .setLoadingListItemCreator(CustomLoadingListItemCreator())
                .build()
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect { uiState ->
                when (uiState) {
                    is Resource.Loading -> {
                        if (uiState.isLoading && viewModel.currentPage == 0) {
                            binding.loading.loadingLayout.visibility = View.VISIBLE
                        } else {
                            binding.loading.loadingLayout.visibility = View.GONE
                        }
                    }
                    is Resource.Success -> {
                        isLoadingPaging = false
                        if (uiState.data.isLastPage) {
                            allItemsLoaded = true
                        }
                        viewModel.currentPage++
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

    private fun subscribeNavigationEvents() {
        sharedViewModel.searchMerchants.observe(viewLifecycleOwner) { searchCriteria ->
            if (searchCriteria != null) {
                resetPaginate()

                tags = searchCriteria.tags
                cityId = searchCriteria.cityId
                merchantName = searchCriteria.searchName

                sharedViewModel.clearSearchFilteredMerchants()
            }
        }
    }

    private fun resetPaginate() {
        merchantsList.clear()
        viewModel.currentPage = 0
        isLoadingPaging = false
        allItemsLoaded = false
    }

    private fun setupClickListeners() {
        binding.apply {
            layoutLocationPermissionGastroPaySdk.root.setOnClickListener {
                locationHelper.requestLocation()
            }
            textViewNearMeGastroPaySdk.setOnClickListener {
                sharedViewModel.pushFragment(SearchMerchantFragment.newInstance())
            }
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