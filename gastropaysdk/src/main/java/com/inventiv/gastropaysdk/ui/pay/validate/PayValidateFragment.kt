package com.inventiv.gastropaysdk.ui.pay.validate

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.inventiv.gastropaysdk.R
import com.inventiv.gastropaysdk.common.BaseFragment
import com.inventiv.gastropaysdk.data.Resource
import com.inventiv.gastropaysdk.data.request.ConfirmProvisionRequest
import com.inventiv.gastropaysdk.data.response.BankCardResponse
import com.inventiv.gastropaysdk.data.response.ProvisionInformationResponse
import com.inventiv.gastropaysdk.databinding.FragmentPayValidateGastropaySdkBinding
import com.inventiv.gastropaysdk.repository.MainRepositoryImp
import com.inventiv.gastropaysdk.repository.PaymentRepositoryImp
import com.inventiv.gastropaysdk.shared.GastroPaySdk
import com.inventiv.gastropaysdk.ui.MainViewModel
import com.inventiv.gastropaysdk.ui.MainViewModelFactory
import com.inventiv.gastropaysdk.ui.pay.result.PayResultFragment
import com.inventiv.gastropaysdk.utils.REFERENCE_UNDEFINED
import com.inventiv.gastropaysdk.utils.blankj.utilcode.util.ConvertUtils
import com.inventiv.gastropaysdk.utils.blankj.utilcode.util.StringUtils
import com.inventiv.gastropaysdk.utils.delegate.viewBinding
import com.inventiv.gastropaysdk.utils.handleError
import com.inventiv.gastropaysdk.utils.itemdecorator.RecyclerMarginDecoration
import com.inventiv.gastropaysdk.view.GastroPaySdkToolbar
import com.inventiv.gastropaysdk.view.YourSpendPointsView
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

internal class PayValidateFragment : BaseFragment(R.layout.fragment_pay_validate_gastropay_sdk) {

    companion object {
        private const val PARAM_TOOLBAR_TITLE = "PARAM_TOOLBAR_TITLE"
        private const val PARAM_PROVISION_DATA = "PARAM_PROVISION_DATA"

        fun newInstance(
            toolbarTitle: String?,
            data: ProvisionInformationResponse
        ) = PayValidateFragment().apply {
            val args = Bundle().apply {
                putString(PARAM_TOOLBAR_TITLE, toolbarTitle)
                putParcelable(PARAM_PROVISION_DATA, data)
            }
            arguments = args
        }
    }

    override fun prepareToolbar(toolbar: GastroPaySdkToolbar, logo: AppCompatImageView) {
        val toolbarTitle = requireArguments().getString(
            PARAM_TOOLBAR_TITLE,
            StringUtils.getString(R.string.payment_confirmation_navigation_title_gastropay_sdk)
        )
        toolbar.apply {
            changeToMainStyle()
            setTitle(toolbarTitle, R.color.white_gastropay_sdk)
            setLeftIcon(R.drawable.ic_close_gastropay_sdk)
            setRightIcon(REFERENCE_UNDEFINED, null)
            onLeftIconClick {
                sharedViewModel.onBackPressed()
            }
        }
        showToolbar(false, toolbar, logo)
    }

    override fun showBottomNavigation() = false

    private val binding by viewBinding(FragmentPayValidateGastropaySdkBinding::bind)

    private lateinit var provision: ProvisionInformationResponse
    private var isPointSpendSelected = false

    private lateinit var bankCardsAdapter: BankCardsAdapter
    private var bankCardsList = ArrayList<BankCardResponse>()

    private val sharedViewModel: MainViewModel by lazy {
        val viewModelFactory = MainViewModelFactory(
            MainRepositoryImp(GastroPaySdk.getComponent().gastroPayService)
        )
        ViewModelProvider(requireActivity(), viewModelFactory).get(MainViewModel::class.java)
    }

    private val viewModel: PayValidateViewModel by lazy {
        val viewModelFactory = PayValidateViewModelFactory(
            PaymentRepositoryImp(GastroPaySdk.getComponent().gastroPayService)
        )
        ViewModelProvider(this, viewModelFactory).get(PayValidateViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        provision = requireArguments().getParcelable(PARAM_PROVISION_DATA)!!

        setupObservers()
        setupCardsAdapter()
        setupUI()

        viewModel.getBankCards()
    }

    private fun setupUI() {
        binding.amountTextView.text = provision.totalAmount.displayValue

        binding.yourSpendPointsView.init(
            listener = object :
                YourSpendPointsView.Listener {
                override fun buttonClicked(isSpendSelected: Boolean) {
                    isPointSpendSelected = isSpendSelected
                    viewModel.provisionInformation(provision.token, isPointSpendSelected)
                }
            }, availableAmount = provision.availableAmount.value,
            displayValue = provision.availableAmount.displayValue
        )

        binding.apply {
            valueOrderTextView.text = provision.amount.displayValue
            valueSumTextView.text = provision.totalAmount.displayValue

            if (isPointSpendSelected) {
                valueSpendPointTextView.text = provision.usingAvailableAmount.displayValue
            } else {
                valueSpendPointTextView.text = "-"
            }
        }
    }

    private fun setupCardsAdapter() {
        bankCardsAdapter = BankCardsAdapter(bankCardsList) { merchant, position ->
            bankCardsList.forEachIndexed { index, item ->
                item.isDefault = false
                if (index == position) {
                    item.isDefault = true
                }
            }
            bankCardsAdapter.notifyDataSetChanged()
            scrollToSelectedCardPosition()
        }
        binding.cardsRecyclerViewGastroPaySdk.addItemDecoration(
            RecyclerMarginDecoration(
                margin = ConvertUtils.dp2px(4f),
                columns = 0
            )
        )
        binding.cardsRecyclerViewGastroPaySdk.adapter = bankCardsAdapter
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            launch {
                viewModel.bankCardsState.collect { uiState ->
                    when (uiState) {
                        is Resource.Loading -> {
                            if (uiState.isLoading) {
                                binding.loading.loadingLayout.visibility = View.VISIBLE
                            } else {
                                binding.loading.loadingLayout.visibility = View.GONE
                            }
                        }
                        is Resource.Success -> {
                            bankCardsList.clear()
                            bankCardsList.addAll(uiState.data)
                            bankCardsAdapter.notifyDataSetChanged()
                            scrollToSelectedCardPosition()
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
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            launch {
                viewModel.provisionInformationState.collect { uiState ->
                    when (uiState) {
                        is Resource.Loading -> {
                            if (uiState.isLoading) {
                                binding.loading.loadingLayout.visibility = View.VISIBLE
                            } else {
                                binding.loading.loadingLayout.visibility = View.GONE
                            }
                        }
                        is Resource.Success -> {
                            provision = uiState.data
                            setupUI()
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

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            launch {
                viewModel.confirmProvisionState.collect { uiState ->
                    when (uiState) {
                        is Resource.Loading -> {
                            if (uiState.isLoading) {
                                binding.loading.loadingLayout.visibility = View.VISIBLE
                            } else {
                                binding.loading.loadingLayout.visibility = View.GONE
                            }
                        }
                        is Resource.Success -> {
                            sharedViewModel.pushFragment(
                                PayResultFragment.newInstance(provision.merchantName, provision)
                            )
                            //TODO is true?
                            GastroPaySdk.getComponent().globalGastroPaySdkListener?.onPaymentSuccess()
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

        binding.payButton.setOnClickListener {
            viewModel.confirmProvision(
                ConfirmProvisionRequest(
                    token = provision.token,
                    cardId = bankCardsList.find { it.isDefault }!!.id,
                    pinCode = null,
                    useCashback = isPointSpendSelected
                )
            )
        }
    }

    private fun scrollToSelectedCardPosition() {
        var position = 0
        bankCardsList.forEachIndexed { index, item ->
            if (item.isDefault) {
                position = index
            }
        }
        binding.cardsRecyclerViewGastroPaySdk.scrollToPosition(position)
    }
}