package com.softeer.team6four.ui.mypage.point

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softeer.team6four.data.Resource
import com.softeer.team6four.data.local.UserPreferencesRepository
import com.softeer.team6four.data.remote.payment.PaymentRepository
import com.softeer.team6four.data.remote.payment.model.PointHistoryDetailModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPointViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val paymentRepository: PaymentRepository
) : ViewModel() {
    private val _myTotalPoint: MutableStateFlow<String> = MutableStateFlow("0")
    val myTotalPoint: StateFlow<String> = _myTotalPoint

    private val _pointHistory: MutableStateFlow<List<PointHistoryDetailModel>> = MutableStateFlow(emptyList())
    val pointHistory: StateFlow<List<PointHistoryDetailModel>> = _pointHistory

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun fetchMyTotalPoint() {
        viewModelScope.launch {
            val accessToken = userPreferencesRepository.getAccessToken().first()
            val myTotalPointData = paymentRepository.getMyTotalPoint1(accessToken)

            myTotalPointData.catch {
                Log.e("MyPointViewModel", "getMyTotalPoint: $this")
            }.collect { totalPoint ->
                when (totalPoint) {
                    is Resource.Success -> {
                        _myTotalPoint.value = totalPoint.data.totalPoint
                    }

                    is Resource.Error -> {
                        if (totalPoint.code == 400) {
                            _myTotalPoint.value = "0 원"
                        } else {
                            Log.e("MyPointViewModel", "getMyTotalPoint: ${totalPoint.message}")
                        }
                    }

                    else -> {
                        _myTotalPoint.value = "0 원"
                    }
                }
            }
        }
    }

    fun fetchPointHistory(lastPaymentId: Long? = null) {
        if (_isLoading.value) return

        _isLoading.value = true

        viewModelScope.launch {
            val accessToken = userPreferencesRepository.getAccessToken().first()
            val pointHistoryData = paymentRepository.getPointHistory(accessToken, lastPaymentId)

            pointHistoryData.catch {
                Log.e("MyPointViewModel", "getPointHistory: $this")
            }.collect { pointHistory ->
                when (pointHistory) {
                    is Resource.Success -> {
                        val newList = pointHistory.data.content.toMutableList()
                        if (pointHistory.data.hasNext) {
                            newList.add(PointHistoryDetailModel(" ", " ", 0, " ", " ", 0))
                        }
                        _pointHistory.value = newList
                        _isLoading.value = false
                    }

                    is Resource.Error -> {
                        Log.e("MyPointViewModel", "getPointHistory: ${pointHistory.message}")
                    }

                    else -> {
                        Log.e("MyPointViewModel", "getPointHistory: $pointHistory")
                    }
                }
            }
        }
    }

    fun chargePoint() {
        viewModelScope.launch {
            val accessToken = userPreferencesRepository.getAccessToken().first()
            val chargePointData = paymentRepository.chargePoint(accessToken, 10000)

            chargePointData.catch {
                Log.e("MyPointViewModel", "chargePoint: $this")
            }.collect { chargePoint ->
                when (chargePoint) {
                    is Resource.Success -> {
                        fetchMyTotalPoint()
                        fetchPointHistory()
                    }

                    is Resource.Error -> {
                        Log.e("MyPointViewModel", "chargePoint: ${chargePoint.message}")
                    }

                    else -> {
                        Log.e("MyPointViewModel", "chargePoint: $chargePoint")
                    }
                }
            }
        }
    }
}