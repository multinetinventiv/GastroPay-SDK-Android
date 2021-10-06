package com.inventiv.gastropaysdk.ui.wallet.detail

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.inventiv.gastropaysdk.R
import com.inventiv.gastropaysdk.common.BaseFragment
import com.inventiv.gastropaysdk.data.Resource
import com.inventiv.gastropaysdk.data.TransactionModel
import com.inventiv.gastropaysdk.databinding.FragmentWalletDetailGastropaySdkBinding
import com.inventiv.gastropaysdk.repository.MainRepositoryImp
import com.inventiv.gastropaysdk.repository.WalletRepositoryImp
import com.inventiv.gastropaysdk.shared.GastroPaySdk
import com.inventiv.gastropaysdk.ui.MainViewModel
import com.inventiv.gastropaysdk.ui.MainViewModelFactory
import com.inventiv.gastropaysdk.ui.wallet.WalletViewModel
import com.inventiv.gastropaysdk.ui.wallet.WalletViewModelFactory
import com.inventiv.gastropaysdk.utils.*
import com.inventiv.gastropaysdk.utils.blankj.utilcode.util.StringUtils
import com.inventiv.gastropaysdk.utils.delegate.viewBinding
import com.inventiv.gastropaysdk.view.GastroPaySdkToolbar
import com.tapadoo.alerter.Alerter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

internal class WalletDetailFragment : BaseFragment(R.layout.fragment_wallet_detail_gastropay_sdk) {

    companion object {
        private const val PARAM_TRANSACTION = "PARAM_TRANSACTION"

        fun newInstance(
            transaction: TransactionModel,
        ) = WalletDetailFragment().apply {
            val args = Bundle().apply {
                putParcelable(PARAM_TRANSACTION, transaction)
            }
            arguments = args
        }
    }

    override fun prepareToolbar(toolbar: GastroPaySdkToolbar, logo: AppCompatImageView) {
        toolbar.apply {
            changeToLoginStyle()
            setTitle(
                StringUtils.getString(R.string.transaction_detail_navigation_title_gastropay_sdk),
                R.color.celtic_gastropay_sdk
            )
            setLeftIcon(R.drawable.ic_arrow_back_gastropay_sdk)
            setRightIcon(REFERENCE_UNDEFINED, null)
            onLeftIconClick {
                sharedViewModel.onBackPressed()
            }
        }
        showToolbar(false, toolbar, logo)
    }

    override fun showBottomNavigation() = false

    private val binding by viewBinding(FragmentWalletDetailGastropaySdkBinding::bind)

    private lateinit var transaction: TransactionModel

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
        transaction = requireArguments().getParcelable(PARAM_TRANSACTION)!!

        binding.apply {
            merchantNameValueTextView.text = transaction.name
            amountValueTextView.text = transaction.price
            dateValueTextView.text = transaction.date.formatDate("dd.MM.yyyy - HH:mm")
            setTransactionType(transactionType = transaction.transactionType)

            if (transaction.invoiceNumber.isNullOrEmpty().not()) {
                invoiceNumberLayout.visibility = View.VISIBLE
                invoiceNumberValueTextView.text = transaction.invoiceNumber
                invoiceNumberLayout.setOnClickListener {
                    viewModel.sendInvoice(transaction.id)
                }
            }
        }
    }

    private fun setTransactionType(transactionType: TransactionType) {
        binding.apply {
            when (transactionType) {
                TransactionType.DEPOSIT -> {
                    transactionTypeValueTextView.text =
                        StringUtils.getString(R.string.wallet_earn_gastropay_sdk)
                    DrawableCompat.setTint(
                        transactionTypeValueTextView.background,
                        ContextCompat.getColor(requireContext(), R.color.shamrock_gastropay_sdk)
                    )
                    transactionTypeValueTextView.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white_gastropay_sdk
                        )
                    )
                }
                TransactionType.WITHDRAW -> {
                    transactionTypeValueTextView.text =
                        StringUtils.getString(R.string.wallet_spend_gastropay_sdk)
                    DrawableCompat.setTint(
                        transactionTypeValueTextView.background,
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.reddish_orange_gastropay_sdk
                        )
                    )
                    transactionTypeValueTextView.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white_gastropay_sdk
                        )
                    )
                }
                TransactionType.CANCEL_DEPOSIT,
                TransactionType.CANCEL_WITHDRAW -> {
                    transactionTypeValueTextView.text =
                        StringUtils.getString(R.string.wallet_cancel_gastropay_sdk)
                    DrawableCompat.setTint(
                        transactionTypeValueTextView.background,
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.light_grey_gastropay_sdk
                        )
                    )
                    transactionTypeValueTextView.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.celtic_gastropay_sdk
                        )
                    )
                }
            }
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            launch {
                viewModel.invoiceSend.collect { uiState ->
                    when (uiState) {
                        is Resource.Loading -> {
                            if (uiState.isLoading) {
                                binding.loading.loadingLayout.visibility = View.VISIBLE
                            } else {
                                binding.loading.loadingLayout.visibility = View.GONE
                            }
                        }
                        is Resource.Success -> {
                            if (uiState.data.isSuccessful) {
                                Alerter.create(requireActivity())
                                    .setTitle(StringUtils.getString(R.string.transaction_detail_navigation_title_gastropay_sdk))
                                    .setText(StringUtils.getString(R.string.transaction_detail_invoice_send_gastropay_sdk))
                                    .setIcon(R.drawable.ic_bank_card_selected_gastropay_sdk)
                                    .setBackgroundColorRes(R.color.shamrock_gastropay_sdk)
                                    .show()
                            } else {
                                val errorResponse = uiState.data.errorBody().bodyToErrorResponse()
                                showError(errorResponse.second)
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
}