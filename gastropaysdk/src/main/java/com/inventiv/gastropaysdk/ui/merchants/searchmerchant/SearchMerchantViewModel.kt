package com.inventiv.gastropaysdk.ui.merchants.searchmerchant

import androidx.lifecycle.viewModelScope
import com.inventiv.gastropaysdk.common.BaseViewModel
import com.inventiv.gastropaysdk.data.Resource
import com.inventiv.gastropaysdk.data.response.CitiesResponse
import com.inventiv.gastropaysdk.data.response.City
import com.inventiv.gastropaysdk.data.response.Tag
import com.inventiv.gastropaysdk.data.response.TagGroupResponse
import com.inventiv.gastropaysdk.repository.MerchantRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

internal class SearchMerchantViewModel(private val merchantRepository: MerchantRepository) :
    BaseViewModel() {

    private val _uiStateCities = MutableStateFlow<Resource<CitiesResponse>>(Resource.Empty)
    val uiStateCities: StateFlow<Resource<CitiesResponse>> get() = _uiStateCities.asStateFlow()

    private val _uiStateSearchCriteria =
        MutableStateFlow<Resource<List<TagGroupResponse>>>(Resource.Empty)
    val uiStateSearchCriteria: StateFlow<Resource<List<TagGroupResponse>>> get() = _uiStateSearchCriteria.asStateFlow()

    private val _selectedCity = MutableStateFlow(City.getDefaultItem())
    val selectedCity: StateFlow<City> get() = _selectedCity.asStateFlow()

    private val _selectedRegion = MutableStateFlow(Tag.getDefaultRegionItem())
    val selectedRegion: StateFlow<Tag> get() = _selectedRegion.asStateFlow()


    fun getSearchCriteria(cityId: String?) {
        viewModelScope.launch {
            merchantRepository.searchCriteria(cityId = cityId).collect { response ->
                _uiStateSearchCriteria.value = response
            }
        }
    }

    fun getCities() {
        viewModelScope.launch {
            merchantRepository.cities().collect { response ->
                _uiStateCities.value = response
            }
        }
    }

    fun selectCity(city: City) {
        _selectedCity.value = city
        getSearchCriteria(
            if (city.isDefaultItem()) {
                null
            } else {
                city.id.toString()
            }
        )
    }

    fun resetCitySelection() {
        selectCity(City.getDefaultItem())
    }

    fun selectRegion(tag: Tag) {
        _selectedRegion.value = tag
    }

    fun resetRegionSelection() {
        _selectedRegion.value = Tag.getDefaultRegionItem()
    }
}