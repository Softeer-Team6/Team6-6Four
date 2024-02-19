package com.softeer.team6four.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naver.maps.geometry.LatLng
import com.softeer.team6four.api.GeoCodeService
import com.softeer.team6four.data.GeoCodeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val geoCodeRepository: GeoCodeRepository
    = GeoCodeRepository(GeoCodeService.create()) //TEST CODE
) : ViewModel() {
    private var _searchCoordinate = MutableStateFlow(LatLng(0.toDouble(), 0.toDouble()))
    val searchCoordinate: StateFlow<LatLng> = _searchCoordinate

    fun getCoordinate(address: String) {
        viewModelScope.launch {
            geoCodeRepository.getCoordinateResult(address).collect { latLng ->
                _searchCoordinate.update { latLng }
            }
        }
    }
}