package com.softeer.team6four.ui.payment

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softeer.team6four.data.Resource
import com.softeer.team6four.data.UserPreferencesRepository
import com.softeer.team6four.data.ReservationRepository
import com.softeer.team6four.data.remote.reservation.model.PaymentInfoModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val reservationRepository: ReservationRepository
) :
    ViewModel() {
    private var _reservationId: MutableStateFlow<Long> = MutableStateFlow(0)
    private val reservationId: StateFlow<Long> = _reservationId

    private var _cipher: MutableStateFlow<String> = MutableStateFlow("")
    private val cipher: StateFlow<String> = _cipher

    private var _paymentInfoModelState: MutableStateFlow<Resource<PaymentInfoModel>> =
        MutableStateFlow(Resource.Loading())
    val paymentInfoModelState: StateFlow<Resource<PaymentInfoModel>> = _paymentInfoModelState

    private var _paymentInfoModel: MutableStateFlow<PaymentInfoModel> = MutableStateFlow(
        PaymentInfoModel()
    )
    val paymentInfoModel: StateFlow<PaymentInfoModel> = _paymentInfoModel
    fun updateReservationId() {
        viewModelScope.launch {
            val token = userPreferencesRepository.getAccessToken().first()
            reservationRepository.getReservationId(token, cipher.value).collect { resource ->
                if (resource is Resource.Success) {
                    _reservationId.value = resource.data
                    Log.d("reservationId", reservationId.value.toString())
                } else if (resource is Resource.Error) {
                    Log.e("getReservationIdError", resource.message)
                }
            }
        }
    }

    fun fetchPaymentInfoModel() {
        viewModelScope.launch {
            val token = userPreferencesRepository.getAccessToken().first()
            reservationRepository.getPaymentInfoModel(token, reservationId.value)
                .collect { resource ->
                    _paymentInfoModelState.value = resource
                    if (resource is Resource.Success) {
                        _paymentInfoModel.value = resource.data
                    }
                }
        }
    }

    fun updateCipher(cipher: String) {
        _cipher.value = cipher
    }

    fun updatePaymentInfoModel() {
        _paymentInfoModelState.value = Resource.Loading()
    }
}