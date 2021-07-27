package com.inventiv.gastropaysdk.ui.wallet

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.ViewModelProvider
import com.inventiv.gastropaysdk.R
import com.inventiv.gastropaysdk.common.BaseFragment
import com.inventiv.gastropaysdk.databinding.FragmentWalletGastropaySdkBinding
import com.inventiv.gastropaysdk.repository.AuthenticationRepositoryImp
import com.inventiv.gastropaysdk.repository.MainRepositoryImp
import com.inventiv.gastropaysdk.shared.GastroPaySdk
import com.inventiv.gastropaysdk.ui.MainViewModel
import com.inventiv.gastropaysdk.ui.MainViewModelFactory
import com.inventiv.gastropaysdk.utils.delegate.viewBinding
import com.inventiv.gastropaysdk.view.GastroPaySdkToolbar

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
            AuthenticationRepositoryImp(GastroPaySdk.getComponent().gastroPayService)
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

        setupMerchantAdapter()

        binding.layoutWalletTransactionDetail.apply {
            valueTextView.text = "231,35"
        }

        setTestData()

        binding.loadingLayout.visibility = View.GONE
    }

    private fun setTestData(){
        transactionsList.add(
            TransactionModel("-26,60 TL", "26.06.2019 - 14:20", "Midpoint", false)
        )
        transactionsList.add(
            TransactionModel("+26,60 TL", "26.06.2019 - 14:20", "Starbucks", true)
        )
        transactionsList.add(
            TransactionModel("-26,60 TL", "26.06.2019 - 14:20", "Midpoint", false)
        )
        transactionsList.add(
            TransactionModel("+26,60 TL", "26.06.2019 - 14:20", "Starbucks", true)
        )
        transactionsList.add(
            TransactionModel("-26,60 TL", "26.06.2019 - 14:20", "Midpoint", false)
        )
        transactionsList.add(
            TransactionModel("+26,60 TL", "26.06.2019 - 14:20", "Starbucks", true)
        )
        transactionsList.add(
            TransactionModel("-26,60 TL", "26.06.2019 - 14:20", "Midpoint", false)
        )
        transactionsList.add(
            TransactionModel("+26,60 TL", "26.06.2019 - 14:20", "Starbucks", true)
        )
        transactionsList.add(
            TransactionModel("-26,60 TL", "26.06.2019 - 14:20", "Midpoint", false)
        )
        transactionsList.add(
            TransactionModel("+26,60 TL", "26.06.2019 - 14:20", "Starbucks", true)
        )
        transactionsList.add(
            TransactionModel("-26,60 TL", "26.06.2019 - 14:20", "Midpoint", false)
        )
        transactionsList.add(
            TransactionModel("+26,60 TL", "26.06.2019 - 14:20", "Starbucks", true)
        )
        transactionsAdapter.notifyDataSetChanged()
    }

    private fun setupMerchantAdapter() {
        transactionsAdapter = TransactionsAdapter(transactionsList) { transaction ->
            /*sharedViewModel.pushFragment(
                MerchantDetailFragment.newInstance(merchant.merchantId)
            )*/
        }
        binding.transactionsRecyclerViewGastroPaySdk.adapter = transactionsAdapter
    }

}