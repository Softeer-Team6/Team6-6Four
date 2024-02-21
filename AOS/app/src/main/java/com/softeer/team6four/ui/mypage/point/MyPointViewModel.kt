package com.softeer.team6four.ui.mypage.point

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softeer.team6four.data.Resource
import com.softeer.team6four.data.local.UserPreferencesRepository
import com.softeer.team6four.data.remote.payment.PaymentRepository
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
    private val _myTotalPoint: MutableStateFlow<Int> = MutableStateFlow(0)
    val myTotalPoint: StateFlow<Int> = _myTotalPoint

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
                            _myTotalPoint.value = 0
                        } else {
                            Log.e("MyPointViewModel", "getMyTotalPoint: ${totalPoint.message}")
                        }
                    }

                    else -> {
                        _myTotalPoint.value = 0
                    }
                }
            }
        }
    }
}