package com.inventiv.gastropaysdk.ui.merchants

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.inventiv.gastropaysdk.R
import com.inventiv.gastropaysdk.common.BaseFragment
import com.inventiv.gastropaysdk.data.model.Resource
import com.inventiv.gastropaysdk.databinding.FragmentMerchantsGastropaySdkBinding
import com.inventiv.gastropaysdk.repository.MainRepositoryImp
import com.inventiv.gastropaysdk.shared.GastroPaySdk
import com.inventiv.gastropaysdk.utils.RecyclerMarginDecoration
import com.inventiv.gastropaysdk.utils.blankj.utilcode.util.ConvertUtils
import com.inventiv.gastropaysdk.utils.blankj.utilcode.util.LogUtils
import com.inventiv.gastropaysdk.utils.viewBinding
import kotlinx.coroutines.flow.collect

internal class MerchantsFragment : BaseFragment(R.layout.fragment_merchants_gastropay_sdk) {

    private val binding by viewBinding(FragmentMerchantsGastropaySdkBinding::bind)

    data class TestMerchant(val name: String, val distance: String, val image: String)

    private lateinit var merchantAdapter: MerchantAdapter
    private var merchantsList = arrayListOf(
        TestMerchant(
            "Starbucks",
            "1.79 km",
            "https://www.coinkolik.com/wp-content/uploads/2021/04/starbucks-btc-bitcoin.jpg"
        ),
        TestMerchant(
            "Starbucks 2",
            "11.79 km",
            "https://media-cdn.tripadvisor.com/media/photo-s/19/59/0e/54/starbucks.jpg"
        ),
        TestMerchant(
            "Starbucks 3",
            "100 km",
            "https://www.coinkolik.com/wp-content/uploads/2021/04/starbucks-btc-bitcoin.jpg"
        ),
        TestMerchant(
            "Starbucks",
            "1.79 km",
            "https://www.coinkolik.com/wp-content/uploads/2021/04/starbucks-btc-bitcoin.jpg"
        ),
        TestMerchant(
            "Starbucks 2",
            "11.79 km",
            "https://media-cdn.tripadvisor.com/media/photo-s/19/59/0e/54/starbucks.jpg"
        ),
        TestMerchant(
            "Starbucks 3",
            "100 km",
            "https://www.coinkolik.com/wp-content/uploads/2021/04/starbucks-btc-bitcoin.jpg"
        ),
        TestMerchant(
            "Starbucks",
            "1.79 km",
            "https://www.coinkolik.com/wp-content/uploads/2021/04/starbucks-btc-bitcoin.jpg"
        ),
        TestMerchant(
            "Starbucks 2",
            "11.79 km",
            "https://media-cdn.tripadvisor.com/media/photo-s/19/59/0e/54/starbucks.jpg"
        ),
        TestMerchant(
            "Starbucks 3",
            "100 km",
            "https://www.coinkolik.com/wp-content/uploads/2021/04/starbucks-btc-bitcoin.jpg"
        ),
    )

    private val viewModel: MerchantsViewModel by lazy {
        val viewModelFactory = MerchantsViewModelFactory(
            MainRepositoryImp(GastroPaySdk.getComponent().gastroPayService)
        )
        ViewModelProvider(this, viewModelFactory).get(MerchantsViewModel::class.java)
    }

    override fun onViewCreated(view: View, bundle: Bundle?) {
        super.onViewCreated(view, bundle)

        setupObservers()
        initMerchantAdapter()
        viewModel.getDummy(1)
    }

    private fun initMerchantAdapter() {
        merchantAdapter = MerchantAdapter(merchantsList) { merchant ->

        }
        binding.merchantsRecyclerView.addItemDecoration(
            RecyclerMarginDecoration(
                ConvertUtils.dp2px(
                    16f
                )
            )
        )
        binding.merchantsRecyclerView.adapter = merchantAdapter
    }

    private fun setupObservers() {
        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect { uiState ->
                when (uiState) {
                    is Resource.Loading -> {
                        LogUtils.d("Resource", uiState.isLoading)
                    }
                    is Resource.Success -> {
                        LogUtils.d("Resource", uiState.data)
                    }
                    is Resource.Error -> {
                        LogUtils.d("Resource", uiState.apiError)
                    }
                    else -> {
                    }
                }
            }
        }
    }
}