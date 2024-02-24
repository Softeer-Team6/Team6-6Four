package com.softeer.team6four.ui.mypage.charger

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softeer.team6four.data.Resource
import com.softeer.team6four.data.local.UserPreferencesRepository
import com.softeer.team6four.data.remote.reservation.ReservationRepository
import com.softeer.team6four.data.remote.reservation.model.ChargerReservationInfoModel
import com.softeer.team6four.data.remote.reservation.model.ReservationTimeModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyChargerReservationViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val reservationRepository: ReservationRepository
) : ViewModel() {
    private val _myChargerReservationList: MutableStateFlow<List<ChargerReservationInfoModel>> = MutableStateFlow(emptyList())
    val myChargerReservationList: StateFlow<List<ChargerReservationInfoModel>> = _myChargerReservationList

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _chargerId: MutableStateFlow<Int> = MutableStateFlow(0)
    val chargerId: StateFlow<Int> = _chargerId

    fun updateChargerId(chargerId: Int) {
        _chargerId.value = chargerId
    }

    fun fetchMyChargerReservationList(chargerId: Int, lastReservationId: Int? = null) {
        if (_isLoading.value) return

        _isLoading.value = true

        viewModelScope.launch {
            val accessToken = userPreferencesRepository.getAccessToken().first()
            val myChargerReservationListData = reservationRepository.getCarbobReservationHistory(
                accessToken,
                chargerId,
                lastReservationId
            )

            myChargerReservationListData.catch {
                Log.e("MyChargerReservation", "getChargerReservationHistory: $this")
            }.collect { chargerReservationList ->
                when (chargerReservationList) {
                    is Resource.Success -> {
                        val newList = chargerReservationList.data.content.toMutableList()
                        if (chargerReservationList.data.hasNext) {
                            newList.add(ChargerReservationInfoModel(
                                " ", " ", " ", " ", " ",
                                0, ReservationTimeModel(" ", " "), 0))
                        }
                        _myChargerReservationList.value = newList
                        _isLoading.value = false
                    }

                    is Resource.Error -> {
                        Log.e(
                            "MyChargerReservation",
                            "getChargerReservationHistory: ${chargerReservationList.message}"
                        )
                    }

                    else -> {
                        Log.e(
                            "MyChargerReservation",
                            "getChargerReservationHistory: $chargerReservationList"
                        )
                    }
                }
            }
        }
    }

    fun updateReservationState(reservationId: Int, stateType: String) {
        viewModelScope.launch {
            val accessToken = userPreferencesRepository.getAccessToken().first()
            val updateReservationStatusResult = reservationRepository.updateReservationStatus(
                accessToken = accessToken,
                reservationId = reservationId,
                status = stateType
            )

            updateReservationStatusResult.catch {
                Log.e("updateReservationStatus", "updateReservationState: $it")
            }.collect { it ->
                when (it) {
                    is Resource.Success -> {
                        val currentList = _myChargerReservationList.value
                        val updatedList = currentList.filterNot { it.reservationId == reservationId }
                        _myChargerReservationList.value = updatedList
                        Log.i("updateReservationStatus", "updateReservationState: ${it.data}")
                    }

                    is Resource.Error -> {
                        Log.e("updateReservationStatus", "updateReservationState: ${it.message}")
                    }

                    else -> {
                        Log.e("updateReservationStatus", "updateReservationState: loading")
                    }
                }
            }
        }
    }
}
