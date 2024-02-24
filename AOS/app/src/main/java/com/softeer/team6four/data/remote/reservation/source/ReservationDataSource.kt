package com.softeer.team6four.data.remote.reservation.source

import com.softeer.team6four.api.ReservationService
import com.softeer.team6four.data.Resource
import com.softeer.team6four.data.remote.reservation.dto.ChargerReservationList
import com.softeer.team6four.data.remote.reservation.dto.ReservationApplyProcess
import com.softeer.team6four.data.remote.reservation.dto.ReservationInfoList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReservationDataSource @Inject constructor(private val reservationService: ReservationService) {

    fun getMyReservationHistory(accessToken: String, sortType: String, lastReservationId: Int?)
     : Flow<Resource<ReservationInfoList>> = flow {
        emit(Resource.Loading())
        val authorization = "Bearer $accessToken"
        try {
            val reservationHistoryResultResponse = reservationService.getReservationHistory(authorization, sortType, lastReservationId)
            if (reservationHistoryResultResponse.isSuccessful) {
                val reservationHistory = reservationHistoryResultResponse.body()?.data
                emit(
                    Resource.Success(
                        reservationHistory ?: throw Exception("Response body is null")
                    )
                )
            } else {
                emit(Resource.Error(reservationHistoryResultResponse.code(), reservationHistoryResultResponse.message()))
            }
        } catch (e: Exception) {
            emit(Resource.Error(message = e.message.toString()))
        }
    }

    fun getCarbobReservationHistory(accessToken: String, carbobId: Int, lastReservationId: Int?)
     : Flow<Resource<ChargerReservationList>> = flow {
        emit(Resource.Loading())
        val authorization = "Bearer $accessToken"
        try {
            val reservationHistoryResultResponse =
                reservationService.getChargerReservation(authorization, carbobId, lastReservationId)
            if (reservationHistoryResultResponse.isSuccessful) {
                val reservationHistory = reservationHistoryResultResponse.body()?.data
                emit(
                    Resource.Success(
                        reservationHistory ?: throw Exception("Response body is null")
                    )
                )
            } else {
                emit(
                    Resource.Error(
                        reservationHistoryResultResponse.code(),
                        reservationHistoryResultResponse.message()
                    )
                )
            }
        } catch (e: Exception) {
            emit(Resource.Error(message = e.message.toString()))
        }
    }

    fun updateReservationStatus(accessToken: String, reservationId: Int, status: String)
    : Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        val authorization = "Bearer $accessToken"
        try {
            val changeReservationStatusResponse =
                reservationService.updateReservationState(authorization, reservationId, ReservationApplyProcess(status))
            if (changeReservationStatusResponse.isSuccessful) {
                emit(Resource.Success(Unit))
            } else {
                emit(
                    Resource.Error(
                        changeReservationStatusResponse.code(),
                        changeReservationStatusResponse.message()
                    )
                )
            }
        } catch (e: Exception) {
            emit(Resource.Error(message = e.message.toString()))
        }
    }
}
