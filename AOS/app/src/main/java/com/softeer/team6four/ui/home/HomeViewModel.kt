package com.softeer.team6four.ui.home

import android.text.Editable
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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val geoCodeRepository: GeoCodeRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val fcmRepository: FcmRepository
) : ViewModel() {
    private var addressText: MutableStateFlow<String> = MutableStateFlow("")

    private var _nickname: MutableStateFlow<String> = MutableStateFlow("")
    val nickname = _nickname


    private var _searchCoordinate = MutableStateFlow(LatLng(0.toDouble(), 0.toDouble()))
    val searchCoordinate: StateFlow<LatLng> = _searchCoordinate

    init {
        updateNickname()
    }
    fun updateAddressText(address: Editable) {
        addressText.value = address.toString()
    }

    fun getCoordinate() {
        viewModelScope.launch {
            geoCodeRepository.getCoordinateResult(addressText.value).collect { latLngResult ->
                latLngResult.onSuccess { latLng ->
                    _searchCoordinate.value = latLng
                }
            }
        }
    }

    fun sendFcmToken(fcmToken: String) {
        viewModelScope.launch {
            val accessToken = userPreferencesRepository.getAccessToken().first()
            val result = fcmRepository.postToken(accessToken, fcmToken)
            result.collect { resource ->
                if (resource is Resource.Error) {
                    Log.e("fcmTokenApi Error", resource.message)
                }
            }
        }
    }

    private fun updateNickname() {
        viewModelScope.launch {
            _nickname.value = userPreferencesRepository.getNickname().first()
        }
    }
}