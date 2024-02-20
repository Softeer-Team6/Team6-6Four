package com.softeer.team6four.api

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.softeer.team6four.data.Resource
import com.softeer.team6four.data.remote.payment.dto.PointChargeDto
import com.softeer.team6four.data.remote.payment.dto.PointHistoryDto
import com.softeer.team6four.data.remote.payment.dto.TotalPointDto
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface PaymentService {

    @POST("total/point")
    suspend fun requestChargePoint(
        @Header("Authorization") token: String,
        @Header("chargePoint") chargePoint: Int
    ): Resource<PointChargeDto>

    @GET("total/point")
    suspend fun getTotalPoint(
        @Header("Authorization") token: String,
    ): Resource<TotalPointDto>

    @GET("my/point")
    suspend fun getPointHistory(
        @Header("Authorization") token: String,
        @Query("paymentId") paymentId: Int
    ): Resource<PointHistoryDto>

    companion object {
        private const val BASE_URL = "http://13.125.3.169:8080/v1/payment/"

        fun create(): PaymentService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
                .build()
                .create(PaymentService::class.java)
        }
    }
}