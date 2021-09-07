package com.inventiv.gastropaysdk.ui.merchants.searchmerchant

import androidx.lifecycle.viewModelScope
import com.inventiv.gastropaysdk.common.BaseViewModel
import com.inventiv.gastropaysdk.data.Resource
import com.inventiv.gastropaysdk.data.response.CitiesResponse
import com.inventiv.gastropaysdk.data.response.City
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

    val selectedCity = MutableStateFlow(City.getDefaultItem())
//    val selectedRegion = MutableStateFlow(Tag.getDefaultRegionItem())


    fun getCities() {
        viewModelScope.launch {
            merchantRepository.cities().collect { response ->
                _uiStateCities.value = response
            }
        }
    }

    fun selectCity(city: City) {
        selectedCity.value = city
        /*searchCriterias(
            if (city.isDefaultItem()) {
                null
            } else {
                city.id.toString()
            }
        )*/
    }

    fun resetCitySelection() {
        selectCity(City.getDefaultItem())
    }

    /*fun selectRegion(tag: Tag) {
        selectedRegion.value = Event(tag)
    }

    fun resetRegionSelection() {
        selectedRegion.value = Event(Tag.getDefaultRegionItem())
    }*/
}