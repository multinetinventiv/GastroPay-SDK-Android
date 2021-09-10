package com.inventiv.gastropaysdk.ui.merchants.searchmerchant

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.chip.Chip
import com.inventiv.gastropaysdk.R
import com.inventiv.gastropaysdk.common.BaseFragment
import com.inventiv.gastropaysdk.data.Resource
import com.inventiv.gastropaysdk.data.request.SearchCriteria
import com.inventiv.gastropaysdk.data.response.*
import com.inventiv.gastropaysdk.databinding.FragmentSearchMerchantGastropaySdkBinding
import com.inventiv.gastropaysdk.repository.MainRepositoryImp
import com.inventiv.gastropaysdk.repository.MerchantRepositoryImp
import com.inventiv.gastropaysdk.shared.GastroPaySdk
import com.inventiv.gastropaysdk.ui.MainViewModel
import com.inventiv.gastropaysdk.ui.MainViewModelFactory
import com.inventiv.gastropaysdk.ui.common.singleselectiondialog.CommonSingleItemSelectionDialogFragment
import com.inventiv.gastropaysdk.utils.COMMA
import com.inventiv.gastropaysdk.utils.blankj.utilcode.util.ConvertUtils
import com.inventiv.gastropaysdk.utils.delegate.viewBinding
import com.inventiv.gastropaysdk.utils.handleError
import com.inventiv.gastropaysdk.utils.itemdecorator.RecyclerHorizontalMarginDecoration
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
    private var regionBottomSheetDialog: CommonSingleItemSelectionDialogFragment<Tag>? = null

    private var selectedCity: City? = null
    private var selectedRegion: Tag? = null

    private var adapterSecondTag: TagAdapter? = null

    override fun prepareToolbar(toolbar: GastroPaySdkToolbar, logo: AppCompatImageView) {
        hideToolbar(toolbar, logo)
    }

    override fun showBottomNavigation() = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSearchToolbar()
        setupRecyclerView()
        setListeners()
        setupObservers()

        viewModel.getSearchCriteria(null)
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

    private fun setupRecyclerView() {
        binding.recyclerSecondTag.addItemDecoration(
            RecyclerHorizontalMarginDecoration(ConvertUtils.dp2px(16f))
        )

        adapterSecondTag = TagAdapter()
        binding.recyclerSecondTag.adapter = adapterSecondTag
    }

    private fun setListeners() {
        binding.apply {
            spinnerCity.setOnClickListener {
                citiesBottomSheetDialog.show(childFragmentManager, TAG_SINGLE_SELECT_CITY_FRAGMENT)
            }
            spinnerRegion.setOnClickListener {
                regionBottomSheetDialog?.show(
                    childFragmentManager,
                    TAG_SINGLE_SELECT_REGION_FRAGMENT
                )
            }
            materialButtonFilter.setOnClickListener {
                var searchName: String? = null

                val query = searchView.query.toString()
                if (query.isNotEmpty()) {
                    searchName = query
                }

                val cityId = if (selectedCity?.isDefaultItem() == true || selectedCity == null) {
                    null
                } else {
                    selectedCity?.id?.toString()
                }

                val tagRegion = selectedRegion?.getRegionTagId()
                val tagCategories = adapterSecondTag?.getTagIds()
                val tagOthers = getChipGroupTagIds()

                val stringBuilder = StringBuilder()

                if (!tagRegion.isNullOrEmpty()) {
                    stringBuilder.append(tagRegion)
                    stringBuilder.append(COMMA)
                }
                if (!tagCategories.isNullOrEmpty()) {
                    stringBuilder.append(tagCategories)
                    stringBuilder.append(COMMA)
                }
                if (tagOthers.isNotEmpty()) {
                    stringBuilder.append(tagOthers)
                    stringBuilder.append(COMMA)
                }

                var strTags = stringBuilder.toString()

                val searchCriteria: SearchCriteria = if (strTags.isNotEmpty()) {
                    strTags = strTags.substring(0, strTags.length - 1)
                    SearchCriteria(tags = strTags, searchName = searchName, cityId = cityId)
                } else {
                    SearchCriteria(searchName = searchName, cityId = cityId)
                }

                sharedViewModel.popFragment(1)
                sharedViewModel.searchFilteredMerchants(searchCriteria)
            }
            materialButtonClearFilter.setOnClickListener {
                adapterSecondTag?.clearTags()
                selectedCity = null
                selectedRegion = null
                viewModel.resetRegionSelection()
                viewModel.resetCitySelection()
            }
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            launch {
                viewModel.uiStateSearchCriteria.collect { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            if (resource.isLoading) {
                                binding.loading.loadingLayout.visibility = View.VISIBLE
                            } else {
                                binding.loading.loadingLayout.visibility = View.GONE
                            }
                        }
                        is Resource.Success -> {
                            val tagGroups = resource.data
                            tagGroups.forEach {
                                when (it.tagGroupKey) {
                                    TagGroupType.REGIONS.value -> {
                                        prepareRegionsBottomSheet(it)
                                    }
                                    TagGroupType.CATEGORIES.value -> {
                                        populateCategoriesTagGroup(it)
                                    }
                                    TagGroupType.OTHERS.value -> {
                                        populateOthersTagGroup(it)
                                    }
                                }
                            }
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
                    viewModel.resetRegionSelection()
                }
            }
            launch {
                viewModel.selectedRegion.collect { region ->
                    selectedRegion = region
                    binding.spinnerRegion.text = region.title
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

    private fun prepareRegionsBottomSheet(tagGroup: TagGroupResponse) {
        regionBottomSheetDialog = CommonSingleItemSelectionDialogFragment.newInstance(
            if (tagGroup.tags?.isNullOrEmpty() == false) {
                binding.spinnerRegion.isClickable = true
                binding.spinnerRegion.isEnabled = true
                tagGroup.tags.apply {
                    add(0, Tag.getDefaultRegionItem())
                }
            } else {
                binding.spinnerRegion.isClickable = false
                binding.spinnerRegion.isEnabled = false
                arrayListOf()
            }
        ) { item, _ ->
            viewModel.selectRegion(item)
        }
    }

    private fun populateCategoriesTagGroup(tagGroup: TagGroupResponse) {
        setCategoriesTagGroupVisibility(tagGroup.tags.isNullOrEmpty().not())

        binding.textSecondTag.text = tagGroup.tagGroupName
        tagGroup.tags?.let { adapterSecondTag?.setTags(it) }
    }

    private fun populateOthersTagGroup(tagGroup: TagGroupResponse) {
        setOthersTagGroupVisibility(tagGroup.tags.isNullOrEmpty().not())

        binding.textThirdTag.text = tagGroup.tagGroupName
        tagGroup.tags?.let { tagList ->
            binding.chipGroupThirdTag.removeAllViews()
            tagList.forEach {
                val chip = Chip(context).apply {
                    text = it.tagName
                    tag = it.id
                    isChipIconVisible = false
                    isCloseIconVisible = false
                    isCheckedIconVisible = false
                    isClickable = true
                    isCheckable = true
                    setTextColor(Color.BLACK)
                    setChipBackgroundColorResource(R.color.selector_background_color_chip_gastropay_sdk)
                }
                binding.chipGroupThirdTag.addView(chip)
            }
        }
    }

    private fun getChipGroupTagIds(): String {
        val checkedChipIds = binding.chipGroupThirdTag.checkedChipIds
        return checkedChipIds.map { viewId ->
            binding.chipGroupThirdTag.findViewById<Chip>(viewId).tag
        }.joinToString(separator = COMMA)
    }

    private fun setCategoriesTagGroupVisibility(isVisible: Boolean) {
        if (isVisible) {
            binding.textSecondTag.visibility = View.VISIBLE
            binding.recyclerSecondTag.visibility = View.VISIBLE
        } else {
            binding.textSecondTag.visibility = View.GONE
            binding.recyclerSecondTag.visibility = View.GONE
        }
    }

    private fun setOthersTagGroupVisibility(isVisible: Boolean) {
        if (isVisible) {
            binding.textThirdTag.visibility = View.VISIBLE
            binding.chipGroupThirdTag.visibility = View.VISIBLE
        } else {
            binding.textThirdTag.visibility = View.GONE
            binding.chipGroupThirdTag.visibility = View.GONE
        }
    }
}