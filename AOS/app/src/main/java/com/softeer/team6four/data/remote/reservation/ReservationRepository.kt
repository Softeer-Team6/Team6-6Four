package com.softeer.team6four.data.remote.reservation

import android.util.Log
import com.softeer.team6four.data.Resource
import com.softeer.team6four.data.remote.reservation.model.ReservationInfoListModel
import com.softeer.team6four.data.remote.reservation.model.ReservationInfoModel
import com.softeer.team6four.data.remote.reservation.model.ReservationTimeModel
import com.softeer.team6four.data.remote.reservation.source.ReservationDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReservationRepository @Inject constructor(private val reservationDataSource: ReservationDataSource) {

    fun getMyReservationHistory(accessToken: String, sortType: String, lastReservationId: Int?)
            : Flow<Resource<ReservationInfoListModel>> {
        return reservationDataSource.getMyReservationHistory(accessToken, sortType, lastReservationId).map { resource ->
            Log.i("sortType", "getMyReservationHistory: $sortType")
            when (resource) {
                is Resource.Success -> {
                    val reservationHistory = resource.data
                    val reservationInfoList = ReservationInfoListModel(
                        reservationHistory.content.map {
                            ReservationInfoModel(
                                it.address,
                                it.carbobImageUrl,
                                it.carbobNickname,
                                it.reservationId,
                                ReservationTimeModel(
                                    it.reservationTime.endTime,
                                    it.reservationTime.startTime
                                ),
                                it.reservationTimeStr,
                                it.stateType,
                                it.totalFee
                            )
                        },
                        reservationHistory.hasNext,
                        reservationHistory.size
                    )
                    Resource.Success(reservationInfoList)
                }

                is Resource.Error -> {
                    Resource.Error(message = resource.message)
                }

                else -> {
                    Resource.Loading()
                }
            }

        }
    }
}
