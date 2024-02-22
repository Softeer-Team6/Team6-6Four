package com.softeer.team6four.data.remote.payment

import com.softeer.team6four.data.Resource
import com.softeer.team6four.data.remote.payment.model.MyTotalPointModel
import com.softeer.team6four.data.remote.payment.model.PointHistoryDetailModel
import com.softeer.team6four.data.remote.payment.model.PointHistoryModel
import com.softeer.team6four.data.remote.payment.source.PaymentDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.DecimalFormat
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PaymentRepository @Inject constructor(private val paymentDataSource: PaymentDataSource) {

    fun getMyTotalPoint1(accessToken: String) : Flow<Resource<MyTotalPointModel>> {
        return paymentDataSource.getMyTotalPoint2(accessToken).map { resource ->
            when (resource) {
                is Resource.Success -> {
                    val totalPoint = resource.data
                    val myTotalPointModel = MyTotalPointModel(formatNumber(totalPoint.totalPoint) + "원")
                    Resource.Success(myTotalPointModel)
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

    fun getPointHistory(accessToken: String, lastPaymentId: Long?) : Flow<Resource<PointHistoryModel>> {
        return paymentDataSource.getPointHistory(accessToken, lastPaymentId).map { resource ->
            when (resource) {
                is Resource.Success -> {
                    val pointHistory = resource.data
                    val pointHistoryModel = PointHistoryModel(
                        pointHistory.content.map {
                            PointHistoryDetailModel(
                                if (it.amount > 0) { "+" + formatNumber(it.amount) + " 원" }
                                else {
                                    formatNumber(it.amount) + " 원" },
                                it.createdDate.split("T")[0].replace("-", ".")
                                    + " "
                                    + it.createdDate.split("T")[1]
                                        .split(".")[0]
                                        .substring(0, 5),
                                it.paymentId,
                                it.paymentType,
                                it.pointTitle,
                                it.targetId
                            )
                        },
                        pointHistory.hasNext,
                        pointHistory.size
                    )
                    Resource.Success(pointHistoryModel)
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

    private fun formatNumber(number: Int): String {
        val formatter = DecimalFormat("#,###")
        return formatter.format(number)
    }

    fun chargePoint(accessToken: String, amount: Int) : Flow<Resource<Unit>> {
        return paymentDataSource.chargePoint(accessToken, amount)
    }

}