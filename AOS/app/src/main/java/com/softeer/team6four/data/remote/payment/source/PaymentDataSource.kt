package com.softeer.team6four.data.remote.payment.source

import android.util.Log
import com.softeer.team6four.api.PaymentService
import com.softeer.team6four.data.Resource
import com.softeer.team6four.data.remote.payment.dto.Point
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
            Log.e("PaymentDataSource", "getMyTotalPoint: $totalPointResultResponse")
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
            Log.e("test", e.stackTraceToString())
            emit(Resource.Error(message = e.message.toString()))
        }

    }
}