package com.softeer.team6four.api

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.softeer.team6four.BuildConfig
import com.softeer.team6four.data.remote.payment.dto.ChargePoint
import com.softeer.team6four.data.remote.payment.dto.PointChargeDto
import com.softeer.team6four.data.remote.payment.dto.PointHistoryDto
import com.softeer.team6four.data.remote.payment.dto.TotalPointDto
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface PaymentService {

    @POST("charge")
    suspend fun requestChargePoint(
        @Header("Authorization") token: String,
        @Body chargePoint: ChargePoint
    ): Response<PointChargeDto>

    @GET("total/point")
    suspend fun getTotalPoint3(
        @Header("Authorization") token: String,
    ): Response<TotalPointDto>

    @GET("my/point")
    suspend fun getPointHistory(
        @Header("Authorization") token: String,
        @Query("lastPaymentId") paymentId: Long?
    ): Response<PointHistoryDto>

    companion object {
        private const val BASE_URL = "${BuildConfig.BASE_URL}/v1/payment/"

        fun create(): PaymentService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
                .build()
                .create(PaymentService::class.java)
        }
    }
}