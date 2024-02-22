package com.softeer.team6four.ui.mypage.reservation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softeer.team6four.data.Resource
import com.softeer.team6four.data.local.UserPreferencesRepository
import com.softeer.team6four.data.remote.reservation.ReservationRepository
import com.softeer.team6four.data.remote.reservation.model.ReservationInfoModel
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
class MyReservationViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val reservationRepository: ReservationRepository
) : ViewModel() {
    private val _myReservationHistory: MutableStateFlow<List<ReservationInfoModel>> = MutableStateFlow(emptyList())
    val myReservationHistory: StateFlow<List<ReservationInfoModel>> = _myReservationHistory

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun fetchMyReservationHistory(sortType: String, lastReservationId: Int? = null) {
        if (_isLoading.value) return

        _isLoading.value = true

        viewModelScope.launch {
            val accessToken = userPreferencesRepository.getAccessToken().first()
            val myReservationHistoryData = reservationRepository.getMyReservationHistory(accessToken, sortType, lastReservationId)

            myReservationHistoryData.catch {
                Log.e("MyReservationViewModel", "getMyReservationHistory: $this")
            }.collect { reservationHistory ->
                when (reservationHistory) {
                    is Resource.Success -> {
                        val newList = reservationHistory.data.content.toMutableList()
                        if (reservationHistory.data.hasNext) {
                            newList.add(ReservationInfoModel(" ", " ", " ", 0,
                                ReservationTimeModel(" ", ""), " ", " ", 0))
                        }
                        _myReservationHistory.value = newList
                        _isLoading.value = false
                    }

                    is Resource.Error -> {
                        Log.e("MyReservationViewModel", "getMyReservationHistory: ${reservationHistory.message}")
                    }

                    else -> {
                        Log.e("MyReservationViewModel", "getMyReservationHistory: $reservationHistory")
                    }
                }
            }
        }
    }
}