package com.softeer.team6four.ui.mypage.charger

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softeer.team6four.data.Resource
import com.softeer.team6four.data.UserPreferencesRepository
import com.softeer.team6four.data.ChargerRepository
import com.softeer.team6four.data.remote.charger.model.MyChargerListModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyChargerListViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val chargerRepository: ChargerRepository
) : ViewModel() {

    private var _myChargerList: MutableStateFlow<Resource<MyChargerListModel>> = MutableStateFlow(
        Resource.Success(
            MyChargerListModel(
                emptyList(), false, 0
            )
        )
    )
    val myChargerList: StateFlow<Resource<MyChargerListModel>> = _myChargerList

    private val _filterText: MutableStateFlow<String> = MutableStateFlow("등록순")
    val filterText: StateFlow<String> = _filterText

    private val _filterState: MutableStateFlow<String> = MutableStateFlow("LATEST")
    val filterState: StateFlow<String> = _filterState

    private val _finishState : MutableStateFlow<Boolean> = MutableStateFlow(false)
    val finishState : StateFlow<Boolean> = _finishState

    private val _refreshState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val refreshState : StateFlow<Boolean> = _refreshState
    fun updateFilterState() {
        if (_filterState.value == "LATEST") {
            _filterText.value = "예약 많은 순"
            _filterState.value = "POPULAR"
        } else {
            _filterText.value = "등록순"
            _filterState.value = "LATEST"
        }
    }

    fun updateFinishState(state : Boolean) {
        _finishState.value = state
    }

    fun updateRefreshState(state : Boolean) {
        _refreshState.value = state
    }
    fun fetchMyChargerList(
        sortType: String,
        lastChargerId: Int? = null,
        lastReservationId: Int? = null
    ) {
        viewModelScope.launch {
            val accessToken = userPreferencesRepository.getAccessToken().first()
            val myChargerListData = chargerRepository.fetchMyChargerList(
                accessToken = accessToken,
                sortType = sortType,
                lastChargerId = lastChargerId,
                lastReservationId = lastReservationId
            )

            myChargerListData.catch {
                Log.e("fetchMyChargerList", "getMyChargerList: $this")
            }.collect { resource ->
                _myChargerList.value = resource
            }
        }
    }

}