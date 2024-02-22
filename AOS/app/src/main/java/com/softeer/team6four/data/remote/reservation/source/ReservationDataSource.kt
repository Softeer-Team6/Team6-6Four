package com.softeer.team6four.data.remote.reservation.source

import com.softeer.team6four.api.ReservationService
import com.softeer.team6four.data.Resource
import com.softeer.team6four.data.remote.reservation.dto.ApplyInfoDto
import com.softeer.team6four.data.remote.reservation.model.ReservationTimeModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ReservationDataSource @Inject constructor(private val reservationService: ReservationService) {
    fun fetchAvailableTime(
        token: String,
        chargerId: Long,
        date: String
    ): Flow<Resource<ReservationTimeModel>> = flow {
        emit(Resource.Loading())
        try {
            val authorization = "bearer $token"
            val response = reservationService.getDateReservationInfo(authorization, chargerId, date)
            if (response.isSuccessful) {
                val timeTableDto = response.body()?.data
                if (timeTableDto != null) {
                    emit(
                        Resource.Success(
                            ReservationTimeModel(
                                timeTableDto.dailyBookedTimeCheck
                            )
                        )
                    )
                } else {
                    emit(Resource.Error(response.code(), "fail to get reservation time"))
                }
            } else {
                emit(Resource.Error(message = "body is null"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(message = e.message.toString()))
        }
    }

    fun postApplyReservation(
        token: String,
        chargerId: Long,
        startTime: String,
        endTime: String
    ): Flow<Resource<Unit>> = flow {
        try {
            val authorization = "bearer $token"
            val response = reservationService.applyReservation(
                authorization,
                ApplyInfoDto(chargerId, startTime, endTime)
            )
            if (response.isSuccessful) {
                emit(Resource.Success(Unit))
            } else {
                emit(Resource.Error(response.code(), message = response.errorBody()?.string()!!))
            }
        } catch (e: Exception) {
            emit(Resource.Error(message = e.message.toString()))
        }
    }
}