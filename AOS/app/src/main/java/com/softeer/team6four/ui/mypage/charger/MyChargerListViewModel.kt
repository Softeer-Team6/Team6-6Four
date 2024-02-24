package com.softeer.team6four.ui.mypage.charger

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softeer.team6four.data.Resource
import com.softeer.team6four.data.local.UserPreferencesRepository
import com.softeer.team6four.data.remote.charger.ChargerRepository
import com.softeer.team6four.data.remote.charger.model.MyChargerSimpleInfoModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyChargerListViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val chargerRepository: ChargerRepository
) : ViewModel() {
    private val _myChargerList: MutableStateFlow<List<MyChargerSimpleInfoModel>> = MutableStateFlow(emptyList())
    val myChargerList: StateFlow<List<MyChargerSimpleInfoModel>> = _myChargerList

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _filterText: MutableStateFlow<String> = MutableStateFlow("등록순")
    val filterText: StateFlow<String> = _filterText

    private val _filterState: MutableStateFlow<String> = MutableStateFlow("LATEST")
    val filterState: StateFlow<String> = _filterState

    fun updateFilterState() {
        if (_filterState.value == "LATEST") {
            _filterText.value = "예약 많은 순"
            _filterState.value = "POPULAR"
        } else {
            _filterText.value = "등록순"
            _filterState.value = "LATEST"
        }
    }

    fun fetchMyChargerList(sortType: String, lastChargerId: Int? = null, lastReservationId: Int? = null) {
        if (_isLoading.value) return

        _isLoading.value = true

        viewModelScope.launch {
            val accessToken = userPreferencesRepository.getAccessToken().first()
            val myChargerListData = chargerRepository.fetchMyChargerList(
                accessToken =  accessToken,
                sortType = sortType,
                lastChargerId = lastChargerId,
                lastReservationId = lastReservationId
            )

            myChargerListData.catch {
                Log.e("fetchMyChargerList", "getMyChargerList: $this")
            }.collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        val newList = resource.data.content.toMutableList()
                        if (resource.data.hasNext) {
                            newList.add(
                                MyChargerSimpleInfoModel(
                                    carbobId = 0,
                                    address = " ",
                                    imageUrl = " ",
                                    nickname = " ",
                                    reservationCount = 0
                                )
                            )
                        }
                        _myChargerList.value = newList
                        _isLoading.value = false
                    }

                    is Resource.Error -> {
                        Log.e("fetchMyChargerList", "getMyChargerList: ${resource.message}")
                    }

                    else -> {
                        Log.e("fetchMyChargerList", "getMyChargerList: $resource")
                    }
                }

            }
        }
    }

}