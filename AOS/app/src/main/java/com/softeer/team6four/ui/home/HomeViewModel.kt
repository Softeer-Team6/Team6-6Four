package com.softeer.team6four.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naver.maps.geometry.LatLng
import com.softeer.team6four.data.remote.geo.GeoCodeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val geoCodeRepository: GeoCodeRepository
) : ViewModel() {
    private var _searchCoordinate = MutableStateFlow(LatLng(0.toDouble(), 0.toDouble()))
    val searchCoordinate: StateFlow<LatLng> = _searchCoordinate

    fun getCoordinate(address: String) {
        viewModelScope.launch {
            geoCodeRepository.getCoordinateResult(address).collect { latLngResult ->
                latLngResult.onSuccess { latLng ->
                    _searchCoordinate.update { latLng }
                }
            }
        }
    }
}