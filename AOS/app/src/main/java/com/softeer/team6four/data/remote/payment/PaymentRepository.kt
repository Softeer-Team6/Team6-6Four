package com.softeer.team6four.data.remote.payment

import com.softeer.team6four.data.Resource
import com.softeer.team6four.data.remote.payment.model.MyTotalPointModel
import com.softeer.team6four.data.remote.payment.source.PaymentDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PaymentRepository @Inject constructor(private val paymentDataSource: PaymentDataSource) {

    fun getMyTotalPoint1(accessToken: String) : Flow<Resource<MyTotalPointModel>> {
        return paymentDataSource.getMyTotalPoint2(accessToken).map { resource ->
            when (resource) {
                is Resource.Success -> {
                    val totalPoint = resource.data
                    val myTotalPointModel = MyTotalPointModel(totalPoint.totalPoint)
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

}