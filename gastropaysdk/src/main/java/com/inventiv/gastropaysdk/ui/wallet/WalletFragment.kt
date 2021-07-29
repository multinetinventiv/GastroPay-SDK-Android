package com.inventiv.gastropaysdk.ui.wallet

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.inventiv.gastropaysdk.R
import com.inventiv.gastropaysdk.common.BaseFragment
import com.inventiv.gastropaysdk.data.TransactionModel
import com.inventiv.gastropaysdk.databinding.FragmentWalletGastropaySdkBinding
import com.inventiv.gastropaysdk.model.Resource
import com.inventiv.gastropaysdk.repository.MainRepositoryImp
import com.inventiv.gastropaysdk.repository.WalletRepositoryImp
import com.inventiv.gastropaysdk.shared.GastroPaySdk
import com.inventiv.gastropaysdk.ui.MainViewModel
import com.inventiv.gastropaysdk.ui.MainViewModelFactory
import com.inventiv.gastropaysdk.utils.delegate.viewBinding
import com.inventiv.gastropaysdk.utils.handleError
import com.inventiv.gastropaysdk.view.GastroPaySdkToolbar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

internal class WalletFragment : BaseFragment(R.layout.fragment_wallet_gastropay_sdk) {

    override fun prepareToolbar(toolbar: GastroPaySdkToolbar, logo: AppCompatImageView) {
        toolbar.changeToMainStyle()
        showToolbar(true, toolbar, logo)
        toolbar.onRightIconClick {
            sharedViewModel.closeSdk()
        }
    }

    override fun showBottomNavigation() = true

    private val binding by viewBinding(FragmentWalletGastropaySdkBinding::bind)

    private val viewModel: WalletViewModel by lazy {
        val viewModelFactory = WalletViewModelFactory(
            WalletRepositoryImp(GastroPaySdk.getComponent().gastroPayService)
        )
        ViewModelProvider(this, viewModelFactory).get(WalletViewModel::class.java)
    }

    private val sharedViewModel: MainViewModel by lazy {
        val viewModelFactory = MainViewModelFactory(
            MainRepositoryImp(GastroPaySdk.getComponent().gastroPayService)
        )
        ViewModelProvider(requireActivity(), viewModelFactory).get(MainViewModel::class.java)
    }

    private lateinit var transactionsAdapter: TransactionsAdapter
    private var transactionsList = ArrayList<TransactionModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
        setupMerchantAdapter()
        viewModel.getWallet()
    }

    private fun setupMerchantAdapter() {
        transactionsAdapter = TransactionsAdapter(transactionsList) { transaction ->
            /*sharedViewModel.pushFragment(
                MerchantDetailFragment.newInstance(merchant.merchantId)
            )*/
        }
        binding.transactionsRecyclerViewGastroPaySdk.adapter = transactionsAdapter
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.lastTransactions.collect { uiState ->
                when (uiState) {
                    is Resource.Loading -> {
                        if (uiState.isLoading) {
                            binding.loadingLayout.visibility = View.VISIBLE
                        } else {
                            binding.loadingLayout.visibility = View.GONE
                        }
                    }
                    is Resource.Success -> {
                        uiState.data.forEach {
                            transactionsList.add(
                                TransactionModel(
                                    name = it.merchantName,
                                    date = it.transactionDate,
                                    price = it.transactionAmount.displayValue,
                                    isEarn = it.transactionAmount.value >= 0
                                )
                            )
                        }
                        transactionsAdapter.notifyDataSetChanged()
                    }
                    is Resource.Error -> {
                        uiState.apiError.handleError(requireActivity())
                    }
                    else -> {
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.wallet.collect { uiState ->
                when (uiState) {
                    is Resource.Loading -> {
                        if (uiState.isLoading) {
                            binding.loadingLayout.visibility = View.VISIBLE
                        } else {
                            binding.loadingLayout.visibility = View.GONE
                        }
                    }
                    is Resource.Success -> {
                        viewModel.getSummary()
                        viewModel.getLastTransactions(
                            uiState.data.walletUId,
                            TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()).toString()
                        )
                    }
                    is Resource.Error -> {
                        uiState.apiError.handleError(requireActivity())
                    }
                    else -> {
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.summary.collect { uiState ->
                when (uiState) {
                    is Resource.Loading -> {
                        if (uiState.isLoading) {
                            binding.loadingLayout.visibility = View.VISIBLE
                        } else {
                            binding.loadingLayout.visibility = View.GONE
                        }
                    }
                    is Resource.Success -> {
                        binding.layoutWalletTransactionDetail.apply {
                            valueTextView.text = uiState.data.totalCashback.value.toString()
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

}