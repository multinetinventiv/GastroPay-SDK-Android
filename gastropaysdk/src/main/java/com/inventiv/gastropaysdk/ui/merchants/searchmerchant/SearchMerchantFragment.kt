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
import com.inventiv.gastropaysdk.data.Resource
import com.inventiv.gastropaysdk.data.response.CitiesResponse
import com.inventiv.gastropaysdk.data.response.City
import com.inventiv.gastropaysdk.databinding.FragmentSearchMerchantGastropaySdkBinding
import com.inventiv.gastropaysdk.repository.MainRepositoryImp
import com.inventiv.gastropaysdk.repository.MerchantRepositoryImp
import com.inventiv.gastropaysdk.shared.GastroPaySdk
import com.inventiv.gastropaysdk.ui.MainViewModel
import com.inventiv.gastropaysdk.ui.MainViewModelFactory
import com.inventiv.gastropaysdk.ui.common.singleselectiondialog.CommonSingleItemSelectionDialogFragment
import com.inventiv.gastropaysdk.utils.delegate.viewBinding
import com.inventiv.gastropaysdk.utils.handleError
import com.inventiv.gastropaysdk.view.GastroPaySdkToolbar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

internal class SearchMerchantFragment :
    BaseFragment(R.layout.fragment_search_merchant_gastropay_sdk) {

    companion object {
        fun newInstance() = SearchMerchantFragment()
        const val TAG_SINGLE_SELECT_CITY_FRAGMENT = "tag_single_select_city_fragment"
        const val TAG_SINGLE_SELECT_REGION_FRAGMENT = "tag_single_select_region_fragment"
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
    private lateinit var citiesBottomSheetDialog: CommonSingleItemSelectionDialogFragment<City>

    private var selectedCity: City? = null
//    private var selectedRegion: Tag? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSearchToolbar()
        setListeners()
        setupObservers()

        viewModel.getCities()
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
            spinnerCity.setOnClickListener {
                citiesBottomSheetDialog.show(childFragmentManager, TAG_SINGLE_SELECT_CITY_FRAGMENT)
            }
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            launch {
                viewModel.uiStateCities.collect { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            if (resource.isLoading) {
                                binding.loading.loadingLayout.visibility = View.VISIBLE
                            } else {
                                binding.loading.loadingLayout.visibility = View.GONE
                            }
                        }
                        is Resource.Success -> {
                            prepareCitiesBottomSheet(resource)
                        }
                        is Resource.Error -> {
                            resource.apiError.handleError(requireActivity())
                        }
                        else -> {
                        }
                    }
                }
            }
            launch {
                viewModel.selectedCity.collect { city ->
                    selectedCity = city
                    binding.spinnerCity.text = city.title
//                    viewModel.resetRegionSelection()
                }
            }
        }
    }

    private fun prepareCitiesBottomSheet(resource: Resource.Success<CitiesResponse>) {
        citiesBottomSheetDialog = CommonSingleItemSelectionDialogFragment.newInstance(
            if (!resource.data.cities.isNullOrEmpty()) {
                binding.spinnerCity.isClickable = true
                binding.spinnerCity.isEnabled = true
                resource.data.cities.apply {
                    add(0, City.getDefaultItem())
                }
            } else {
                binding.spinnerCity.isClickable = false
                binding.spinnerCity.isEnabled = false
                arrayListOf()
            }
        ) { item, _ ->
            viewModel.selectCity(item)
        }
    }

    override fun prepareToolbar(toolbar: GastroPaySdkToolbar, logo: AppCompatImageView) {
        hideToolbar(toolbar, logo)
    }

    override fun showBottomNavigation() = false
}