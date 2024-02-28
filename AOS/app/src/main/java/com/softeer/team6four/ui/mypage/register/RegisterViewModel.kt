package com.softeer.team6four.ui.mypage.register

import android.text.Editable
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naver.maps.geometry.LatLng
import com.softeer.team6four.data.Resource
import com.softeer.team6four.data.UserPreferencesRepository
import com.softeer.team6four.data.ChargerRepository
import com.softeer.team6four.data.remote.charger.model.RegistrationModel
import com.softeer.team6four.data.GeoCodeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val chargerRepository: ChargerRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val geoCodeRepository: GeoCodeRepository
) :
    ViewModel() {
    private val _firstConditionFirstState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val firstConditionFirstState: StateFlow<Boolean> = _firstConditionFirstState

    private val _firstConditionSecondState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val firstConditionSecondState: StateFlow<Boolean> = _firstConditionSecondState

    private val _secondConditionFirstState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val secondConditionFirstState: StateFlow<Boolean> = _secondConditionFirstState

    private val _secondConditionSecondState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val secondConditionSecondState: StateFlow<Boolean> = _secondConditionSecondState

    private val _addressText: MutableStateFlow<String> = MutableStateFlow("")
    val addressText: StateFlow<String> = _addressText


    private val _userLatLng: MutableStateFlow<LatLng?> =
        MutableStateFlow(null)
    val userLatLng: StateFlow<LatLng?> = _userLatLng

    private val _chargerNickname: MutableStateFlow<String> = MutableStateFlow("")
    val chargerNickname: StateFlow<String> = _chargerNickname

    private val _locationType: MutableStateFlow<String> = MutableStateFlow("")
    val locationType: StateFlow<String> = _locationType

    private val _chargerType: MutableStateFlow<String> = MutableStateFlow("")
    val chargerType: StateFlow<String> = _chargerType

    private val _speedType: MutableStateFlow<String> = MutableStateFlow("")
    val speedType: StateFlow<String> = _speedType

    private val _installType: MutableStateFlow<String> = MutableStateFlow("")
    val installType: StateFlow<String> = _installType

    private val _startDateTime: MutableStateFlow<Int> = MutableStateFlow(-1)
    val startDateTime: StateFlow<Int> = _startDateTime

    private val _endDateTime: MutableStateFlow<Int> = MutableStateFlow(-1)
    val endDateTime: StateFlow<Int> = _endDateTime

    private val _imageUrl: MutableStateFlow<String> = MutableStateFlow("")
    val imageUrl: StateFlow<String> = _imageUrl

    private val _realPath: MutableStateFlow<String> = MutableStateFlow("")
    val realPath: StateFlow<String> = _realPath

    private val _description: MutableStateFlow<String> = MutableStateFlow("")
    val description: StateFlow<String> = _description

    private val _price: MutableStateFlow<String> = MutableStateFlow("")
    val price: StateFlow<String> = _price

    private val _chargerTypeText: MutableStateFlow<String> = MutableStateFlow("")
    val chargerTypeText: StateFlow<String> = _chargerTypeText

    private val _speedTypeText: MutableStateFlow<String> = MutableStateFlow("")
    val speedTypeText: StateFlow<String> = _speedTypeText

    private val _locationTypeText: MutableStateFlow<String> = MutableStateFlow("")
    val locationTypeText: StateFlow<String> = _locationTypeText

    private val _installTypeText: MutableStateFlow<String> = MutableStateFlow("")
    val installTypeText: StateFlow<String> = _installTypeText


    fun setFirstConditionFirstState() {
        _firstConditionFirstState.value = true
        _firstConditionSecondState.value = false
    }

    fun setFirstConditionSecondState() {
        _firstConditionSecondState.value = true
        _firstConditionFirstState.value = false
    }

    fun setSecondConditionFirstState() {
        _secondConditionFirstState.value = true
        _secondConditionSecondState.value = false
    }

    fun setSecondConditionSecondState() {
        _secondConditionSecondState.value = true
        _secondConditionFirstState.value = false
    }

    fun updateUserLng(position: LatLng) {
        _userLatLng.value = position
    }

    fun updateAddressText(text: Editable) {
        _addressText.value = text.toString()
    }

    fun getCoordinate() {
        viewModelScope.launch {
            geoCodeRepository.getCoordinateResult(addressText.value).collect { result ->
                result.onSuccess { latLng ->
                    Log.d("latLng", latLng.toString())
                    _userLatLng.value = latLng
                }.onFailure { e ->
                    Log.e("get LatLng Error", e.message.toString())
                }
            }
        }
    }

    fun updateNickname(text: Editable) {
        _chargerNickname.value = text.toString()
    }

    fun updateDescription(text: Editable) {
        _description.value = text.toString()
    }

    fun updateLocationType(type: String) {
        _locationTypeText.value =
            when (type) {
                "HOUSE" -> "단독주택"
                "VILLA" -> "빌라"
                "APARTMENT" -> "아파트"
                else -> "기타"
            }
        _locationType.value = type
    }

    fun updateImgUrl(url: String) {
        _imageUrl.value = url
    }

    fun updateChargerType(type: String) {
        _chargerTypeText.value =
            when (type) {
                "SLOW" -> "완속"
                "AC3" -> "AC3상"
                "DESTINATION" -> "급속"
                else -> "기타"
            }
        _chargerType.value = type
    }

    fun updateInstallType(type: String) {
        _installTypeText.value =
            when (type) {
                "OUTDOOR" -> "실내"
                "INDOOR" -> "외부"
                "CANOPY" -> "캐노피"
                else -> "기타"
            }
        _installType.value = type
    }

    fun updateSpeedType(type: String) {
        _speedTypeText.value =
            when (type) {
                "KWH3" -> "3kW"
                "KWH5" -> "5kW"
                "KwH7" -> "7kW"
                else -> "11kW"
            }
        _speedType.value = type
    }

    fun updatePrice(text: Editable) {
        _price.value = text.toString()
    }

    fun updateStartTime(time: Editable) {
        if (time.isNotEmpty()) {
            _startDateTime.value = time.toString().toInt()
        }
    }

    fun updateEndTime(time: Editable) {
        if (time.isNotEmpty()) {
            _endDateTime.value = time.toString().toInt()
        }
    }

    fun isDetailCompleted(): StateFlow<Boolean> {
        return chargerNickname.combine(locationType) { first, second ->
            first.isNotEmpty() && second.isNotEmpty()
        }.combine(chargerType) { first, second ->
            first && second.isNotEmpty()
        }.combine(speedType) { first, second ->
            first && second.isNotEmpty()
        }.combine(installType) { first, second ->
            first && second.isNotEmpty()
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            false
        )
    }

    fun getPricePerHour(): StateFlow<String> {
        return price.combine(speedType) { p, s ->
            if (p.isEmpty()) {
                "가격"
            } else {
                p.toIntOrNull()?.let { price ->
                    val unit = when (s) {
                        "KWH3" -> 3
                        "KWH5" -> 5
                        "KWH7" -> 7
                        else -> 11
                    }
                    price * unit
                }.toString()
            }
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            "가격"
        )
    }

    fun uploadImage() {
        viewModelScope.launch {
            chargerRepository.uploadImage(
                userPreferencesRepository.getAccessToken().first(),
                realPath.value
            ).collect { resultUrl ->
                if (resultUrl is Resource.Success) {
                    updateImgUrl(resultUrl.data)
                } else if (resultUrl is Resource.Error) {
                    Log.e("uploadImageError", resultUrl.message)
                }
            }
        }
    }

    fun updateRealPath(realPath: String) {
        _realPath.value = realPath
    }

    fun updateAddressText(text: String) {
        _addressText.value = text
    }

    fun registerCharger() {
        viewModelScope.launch {
            chargerRepository.registerCharger(
                userPreferencesRepository.getAccessToken().first(),
                RegistrationModel(
                    chargerNickname = chargerNickname.value,
                    feePer1kWh = price.value.toInt(),
                    description = description.value,
                    address = addressText.value,
                    latitude = userLatLng.value?.latitude ?: 0.0,
                    longitude = userLatLng.value?.longitude ?: 0.0,
                    locationType = locationType.value,
                    speedType = speedType.value,
                    installType = installType.value,
                    startTime = startDateTime.value,
                    endTime = endDateTime.value,
                    chargeType = chargerType.value,
                    imageUrl = imageUrl.value
                )
            ).collect { resource ->
                if (resource is Resource.Error) {
                    Log.e(
                        "registerChargerError",
                        "code : ${resource.code} message: ${resource.message}"
                    )
                }
            }
        }
    }
}