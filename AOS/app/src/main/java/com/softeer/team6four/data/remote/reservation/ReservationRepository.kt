package com.softeer.team6four.data.remote.reservation

import com.softeer.team6four.data.Resource
import com.softeer.team6four.data.remote.reservation.model.ReservationTimeModel
import com.softeer.team6four.data.remote.reservation.source.ReservationDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReservationRepository @Inject constructor(private val reservationDataSource: ReservationDataSource) {
    fun fetchReservationTimeModel(
        token: String,
        chargerId: Long,
        date: String
    ): Flow<Resource<ReservationTimeModel>> {
        return reservationDataSource.fetchAvailableTime(token, chargerId, date)
    }

    fun postApplyReservation(
        token: String, chargerId: Long,
        startTime: String, endTime: String
    ): Flow<Resource<Unit>> {
        return reservationDataSource.postApplyReservation(token, chargerId, startTime, endTime)
    }
}