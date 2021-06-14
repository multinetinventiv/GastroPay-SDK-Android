package com.inventiv.gastropaysdk.ui.merchants

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.inventiv.gastropaysdk.R
import com.inventiv.gastropaysdk.common.BaseFragment
import com.inventiv.gastropaysdk.data.model.Resource
import com.inventiv.gastropaysdk.data.model.response.Merchant
import com.inventiv.gastropaysdk.databinding.FragmentMerchantsGastropaySdkBinding
import com.inventiv.gastropaysdk.repository.MerchantRepositoryImp
import com.inventiv.gastropaysdk.shared.GastroPaySdk
import com.inventiv.gastropaysdk.utils.CustomLoadingListItemCreator
import com.inventiv.gastropaysdk.utils.RecyclerMarginDecoration
import com.inventiv.gastropaysdk.utils.blankj.utilcode.util.ConvertUtils
import com.inventiv.gastropaysdk.utils.blankj.utilcode.util.LogUtils
import com.inventiv.gastropaysdk.utils.viewBinding
import com.paginate.Paginate
import kotlinx.coroutines.flow.collect

internal class MerchantsFragment : BaseFragment(R.layout.fragment_merchants_gastropay_sdk) {

    private val binding by viewBinding(FragmentMerchantsGastropaySdkBinding::bind)

    data class LatLong(val lat: Double, val long: Double)

    private lateinit var merchantAdapter: MerchantAdapter
    private lateinit var merchantPaginate: Paginate
    private var merchantsList = ArrayList<Merchant>()
    private var isLoadingPaging = false
    private var allItemsLoaded = false
    private var myPosition = LatLong(41.0032993914033,29.02845396783002)
    private val paginateWinCallbacks = object : Paginate.Callbacks {
        override fun onLoadMore() {
            isLoadingPaging = true
            viewModel.getMerchants(myPosition)
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

    override fun onViewCreated(view: View, bundle: Bundle?) {
        super.onViewCreated(view, bundle)

        setupObservers()
        setupMerchantAdapter()
        viewModel.getMerchants(myPosition)
    }

    private fun setupMerchantAdapter() {
        merchantAdapter = MerchantAdapter(merchantsList) { merchant ->
            LogUtils.d("Clicked", merchant)
        }
        binding.merchantsRecyclerView.addItemDecoration(
            RecyclerMarginDecoration(
                ConvertUtils.dp2px(
                    16f
                )
            )
        )
        binding.merchantsRecyclerView.adapter = merchantAdapter
        merchantPaginate = Paginate.with(binding.merchantsRecyclerView, paginateWinCallbacks)
            .setLoadingTriggerThreshold(1)
            .addLoadingListItem(true)
            .setLoadingListItemCreator(CustomLoadingListItemCreator())
            .build()
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

                        isLoadingPaging = false
                        if (uiState.data.isLastPage) {
                            allItemsLoaded = true
                        }
                        viewModel.currentPage = uiState.data.pageIndex + 1
                        merchantsList.addAll(uiState.data.merchants)
                        merchantAdapter.notifyDataSetChanged()
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
}