package com.softeer.team6four.data.remote.payment.source

import android.util.Log
import com.softeer.team6four.api.PaymentService
import com.softeer.team6four.data.Resource
import com.softeer.team6four.data.remote.payment.dto.ChargePoint
import com.softeer.team6four.data.remote.payment.dto.Point
import com.softeer.team6four.data.remote.payment.dto.PointDetailList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PaymentDataSource @Inject constructor(private val paymentService: PaymentService) {

    fun getMyTotalPoint2(accessToken: String) : Flow<Resource<Point>> = flow {
        emit(Resource.Loading())
        val authorization = "Bearer $accessToken"
        try {
            val totalPointResultResponse = paymentService.getTotalPoint3(authorization)
            if (totalPointResultResponse.isSuccessful) {
                val totalPoint = totalPointResultResponse.body()?.data
                emit(
                    Resource.Success(
                        totalPoint ?: throw Exception("Response body is null")
                    )
                )
            } else {
                emit(Resource.Error(totalPointResultResponse.code(), totalPointResultResponse.message()))
            }
        } catch (e: Exception) {
            emit(Resource.Error(message = e.message.toString()))
        }
    }

    fun getPointHistory(accessToken: String, lastPaymentId: Long?) : Flow<Resource<PointDetailList>> = flow {
        emit(Resource.Loading())
        val authorization = "Bearer $accessToken"
        try {
            val pointHistoryResultResponse = paymentService.getPointHistory(authorization, lastPaymentId)
            if (pointHistoryResultResponse.isSuccessful) {
                val pointHistory = pointHistoryResultResponse.body()?.data
                emit(
                    Resource.Success(
                        pointHistory ?: throw Exception("Response body is null")
                    )
                )
            } else {
                emit(Resource.Error(pointHistoryResultResponse.code(), pointHistoryResultResponse.message()))
            }
        } catch (e: Exception) {
            emit(Resource.Error(message = e.message.toString()))
        }
    }

    fun chargePoint(accessToken: String, amount: Int) : Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        val authorization = "Bearer $accessToken"
        try {
            val chargePointResultResponse = paymentService.requestChargePoint(authorization, ChargePoint(amount))
            if (chargePointResultResponse.isSuccessful) {
                emit(Resource.Success(Unit))
            } else {
                emit(
                    Resource.Error(
                        chargePointResultResponse.code(),
                        chargePointResultResponse.message()
                    )
                )
            }
        } catch (e: Exception) {
            Log.e("test", e.stackTraceToString())
            emit(Resource.Error(message = e.message.toString()))
        }
    }
}