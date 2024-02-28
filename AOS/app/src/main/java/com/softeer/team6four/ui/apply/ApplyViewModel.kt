package com.softeer.team6four.ui.apply

import android.text.Editable
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softeer.team6four.data.Resource
import com.softeer.team6four.data.local.UserPreferencesRepository
import com.softeer.team6four.data.remote.reservation.ReservationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ApplyViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val reservationRepository: ReservationRepository
) :
    ViewModel() {
    private var _selectedChargerId: MutableStateFlow<Long> = MutableStateFlow(0)
    private val selectedChargerId: StateFlow<Long> = _selectedChargerId

    private var _selectedDate: MutableStateFlow<Date> =
        MutableStateFlow(Date(System.currentTimeMillis()))
    val selectedDate: StateFlow<Date> = _selectedDate

    private var _reservationTime: MutableStateFlow<List<Boolean>> =
        MutableStateFlow(List(24) { false })
    val reservationTime: StateFlow<List<Boolean>> = _reservationTime

    private var _startTime: MutableStateFlow<Int> = MutableStateFlow(0)
    val startTime: StateFlow<Int> = _startTime

    private var _endTime: MutableStateFlow<Int> = MutableStateFlow(0)
    val endTime: StateFlow<Int> = _endTime

    fun updateStartTime(time: Editable) {
        _startTime.value = time.toString().toInt()
    }

    fun updateEndTime(time: Editable) {
        _endTime.value = time.toString().toInt()
    }

    fun updateSelectedChargerId(chargerId: Long) {
        _selectedChargerId.value = chargerId
    }

    fun increaseSelectedDate() {
        val calendar = Calendar.getInstance().apply {
            time = selectedDate.value
        }
        calendar.add(Calendar.DATE, 1)
        _selectedDate.value = calendar.time
    }

    fun decreaseSelectedDate() {
        val calendar = Calendar.getInstance().apply {
            time = selectedDate.value
        }
        calendar.add(Calendar.DATE, -1)
        _selectedDate.value = calendar.time
    }

    fun fetchReservationTime() {
        viewModelScope.launch {
            val token = userPreferencesRepository.getAccessToken().first()
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val time = sdf.format(selectedDate.value)
            reservationRepository.fetchReservationTimeModel(
                token,
                selectedChargerId.value,
                time
            ).collect { resource ->
                if (resource is Resource.Success) {
                    _reservationTime.value = resource.data.timetable

                } else if (resource is Resource.Error) {
                    Log.e("reservationTime", resource.message)
                }
            }
        }
    }

    fun applyReservation() {
        viewModelScope.launch {
            val token = userPreferencesRepository.getAccessToken().first()
            val timeDate =
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(selectedDate.value)

            val convertedStartTime =
                "${timeDate}T${String.format("%02d", startTime.value)}:00:00.000000"
            val convertedEndTime =
                "${timeDate}T${String.format("%02d", endTime.value)}:00:00.000000"

            Log.d("convertedStartTime", convertedStartTime)
            reservationRepository.postApplyReservation(
                token,
                selectedChargerId.value,
                convertedStartTime,
                convertedEndTime
            ).collect { resource ->
                if (resource is Resource.Success) {
                    Log.d("apply reservation", "success")
                } else if (resource is Resource.Error) {
                    Log.e("apply reservation", resource.message)
                }
            }
        }

    }

}