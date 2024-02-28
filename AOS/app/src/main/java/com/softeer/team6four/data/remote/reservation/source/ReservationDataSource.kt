package com.softeer.team6four.data.remote.reservation.source

import com.softeer.team6four.api.ReservationService
import com.softeer.team6four.data.Resource
import com.softeer.team6four.data.remote.reservation.dto.ApplyInfoDto
import com.softeer.team6four.data.remote.reservation.dto.ChargerReservationList
import com.softeer.team6four.data.remote.reservation.dto.ReservationApplyProcess
import com.softeer.team6four.data.remote.reservation.dto.ReservationIdDto
import com.softeer.team6four.data.remote.reservation.dto.ReservationInfoList
import com.softeer.team6four.data.remote.reservation.model.AvailableTimeTableModel
import com.softeer.team6four.data.remote.reservation.model.PaymentInfoModel
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
            val reservationHistoryResultResponse =
                reservationService.getReservationHistory(authorization, sortType, lastReservationId)
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
                reservationService.updateReservationState(
                    authorization,
                    reservationId,
                    ReservationApplyProcess(status)
                )
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

    fun fetchAvailableTime(
        token: String,
        chargerId: Long,
        date: String
    ): Flow<Resource<AvailableTimeTableModel>> = flow {
        emit(Resource.Loading())
        try {
            val authorization = "Bearer $token"
            val response = reservationService.getDateReservationInfo(authorization, chargerId, date)
            if (response.isSuccessful) {
                val timeTableDto = response.body()?.data
                if (timeTableDto != null) {
                    emit(
                        Resource.Success(
                            AvailableTimeTableModel(
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
            val authorization = "Bearer $token"
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

    fun getReservationId(token: String, cipher: String): Flow<Resource<Long>> = flow {
        try {
            val authorization = "Bearer $token"
            val response = reservationService.getVerification(
                authorization,
                cipher
            )
            if (response.isSuccessful) {
                val data = response.body()?.data
                if (data == null) {
                    emit(Resource.Error(message = "body is null"))
                } else {
                    emit(
                        if (!data.isVerified) Resource.Error(
                            response.code(),
                            message = response.errorBody()?.string()!!
                        ) else
                            Resource.Success(data.reservationId)
                    )
                }
            } else {
                emit(Resource.Error(response.code(), message = response.errorBody()?.string()!!))
            }
        } catch (e: Exception) {
            emit(Resource.Error(message = e.message.toString()))
        }
    }

    fun getPaymentInfoModel(
        accessToken: String,
        reservationId: Long
    ): Flow<Resource<PaymentInfoModel>> = flow {
        emit(Resource.Loading())
        try {
            val authorization = "Bearer $accessToken"
            val chargeInfoModelResponse =
                reservationService.getPaymentInfo(authorization, ReservationIdDto(reservationId))
            if (chargeInfoModelResponse.isSuccessful) {
                val data = chargeInfoModelResponse.body()?.data
                if (data == null) {
                    emit(Resource.Error(message = "Body is null"))
                } else {
                    emit(
                        Resource.Success(
                            PaymentInfoModel(
                                data.address,
                                data.estimatedChargeAmount,
                                data.remainPoint,
                                data.rentalPoint,
                                data.reservationTime
                            )
                        )
                    )
                }
            } else {
                emit(
                    Resource.Error(
                        chargeInfoModelResponse.code(),
                        message = chargeInfoModelResponse.errorBody()?.string()!!
                    )
                )
            }
        } catch (e: Exception) {
            emit(Resource.Error(message = e.message.toString()))
        }
    }
}
