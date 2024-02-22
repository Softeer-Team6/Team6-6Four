package com.softeer.team6four.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naver.maps.geometry.LatLng
import com.softeer.team6four.data.Resource
import com.softeer.team6four.data.local.UserPreferencesRepository
import com.softeer.team6four.data.remote.fcm.FcmRepository
import com.softeer.team6four.data.remote.geo.GeoCodeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val geoCodeRepository: GeoCodeRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val fcmRepository: FcmRepository
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

    fun sendFcmToken(fcmToken: String) {
        viewModelScope.launch {
            val accessToken = userPreferencesRepository.getAccessToken().first()
            val result = fcmRepository.postToken(accessToken, fcmToken)
            result.collect {resource ->
                if(resource is Resource.Error) {
                    Log.e("fcmTokenApi Error",resource.message)
                }
            }
        }
    }

}