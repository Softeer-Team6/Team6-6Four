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
        viewModelScope.launch {
            val accessToken = userPreferencesRepository.getAccessToken().first()
            val pointHistoryData = paymentRepository.getPointHistory(accessToken, lastPaymentId)

            pointHistoryData.catch {
                Log.e("MyPointViewModel", "getPointHistory: $this")
            }.collect { pointHistory ->
                when (pointHistory) {
                    is Resource.Success -> {
                        _pointHistory.value = pointHistory.data.content
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