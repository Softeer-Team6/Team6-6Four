package com.softeer.team6four.data

import android.util.Log
import com.softeer.team6four.data.remote.reservation.model.AvailableTimeTableModel
import com.softeer.team6four.data.remote.reservation.model.ChargerReservationInfoModel
import com.softeer.team6four.data.remote.reservation.model.ChargerReservationListModel
import com.softeer.team6four.data.remote.reservation.model.PaymentInfoModel
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
        return reservationDataSource.getMyReservationHistory(
            accessToken,
            sortType,
            lastReservationId
        ).map { resource ->
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

    fun getCarbobReservationHistory(accessToken: String, carbobId: Int, lastReservationId: Int?)
            : Flow<Resource<ChargerReservationListModel>> {
        return reservationDataSource.getCarbobReservationHistory(
            accessToken,
            carbobId,
            lastReservationId
        ).map { resource ->
            when (resource) {
                is Resource.Success -> {
                    val chargerReservationHistory = resource.data
                    val chargerReservationListModel = ChargerReservationListModel(
                        chargerReservationHistory.content.map {
                            ChargerReservationInfoModel(
                                address = it.address,
                                carbobNickname = it.carbobNickname,
                                guestNickname = it.guestNickname,
                                rentalDate = it.rentalDate,
                                rentalTime = it.rentalTime,
                                reservationId = it.reservationId,
                                reservationTime = ReservationTimeModel(
                                    it.reservationTime.endTime,
                                    it.reservationTime.startTime
                                ),
                                totalFee = it.totalFee
                            )
                        },
                        chargerReservationHistory.hasNext,
                        chargerReservationHistory.size
                    )
                    Resource.Success(chargerReservationListModel)
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

    fun updateReservationStatus(accessToken: String, reservationId: Int, status: String)
            : Flow<Resource<Unit>> {
        return reservationDataSource.updateReservationStatus(accessToken, reservationId, status)
            .map { resource ->
                when (resource) {
                    is Resource.Success -> {
                        Resource.Success(Unit)
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

    fun fetchReservationTimeModel(
        token: String,
        chargerId: Long,
        date: String
    ): Flow<Resource<AvailableTimeTableModel>> {
        return reservationDataSource.fetchAvailableTime(token, chargerId, date)
    }

    fun postApplyReservation(
        token: String, chargerId: Long,
        startTime: String, endTime: String
    ): Flow<Resource<Unit>> {
        return reservationDataSource.postApplyReservation(token, chargerId, startTime, endTime)
    }

    fun getReservationId(token: String, cipher: String): Flow<Resource<Long>> {
        return reservationDataSource.getReservationId(token, cipher)
    }

    fun getPaymentInfoModel(token: String, reservationId: Long): Flow<Resource<PaymentInfoModel>> {
        return reservationDataSource.getPaymentInfoModel(token, reservationId)
    }
}
