package com.softeer.team6four.api

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.softeer.team6four.BuildConfig
import com.softeer.team6four.data.remote.reservation.dto.ChargerReservationListDto
import com.softeer.team6four.data.remote.reservation.dto.DateReservationInfoDto
import com.softeer.team6four.data.remote.reservation.dto.PaymentInfo
import com.softeer.team6four.data.remote.reservation.dto.ReservationApplyProcess
import com.softeer.team6four.data.remote.reservation.dto.ReservationConfirmationDto
import com.softeer.team6four.data.remote.reservation.dto.ReservationHistoryDto
import com.softeer.team6four.data.remote.reservation.dto.ReservationRequestDto
import com.softeer.team6four.data.remote.reservation.dto.VerificationDto
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ReservationService {

    @GET("{carbobId}/daily")
    suspend fun getDateReservationInfo(
        @Header("Authorization") token: String,
        @Path("carbobId") carbobId: Int,
        @Query("date") date: String?
    ): Response<DateReservationInfoDto>

    @PATCH("check/{reservationId}")
    suspend fun updateReservationState(
        @Header("Authorization") token: String,
        @Path("reservationId") reservationId: Int,
        @Body stateType: ReservationApplyProcess
    ): Response<ReservationConfirmationDto>

    @POST("apply")
    suspend fun applyReservation(
        @Header("Authorization") token: String,
        @Field("carbobId") carbobId: Long,
        @Field("startDateTime") startDateTime : String,
        @Field("endDateTime") endDateTime : String
    ): Response<ReservationRequestDto>

    @GET("verification")
    suspend fun getVerification(
        @Header("Authorization") token: String,
        @Header("cipher") cipher: String
    ): Response<VerificationDto>

    @POST("fulfillment")
    suspend fun fulfillVerification(
        @Header("Authorization") token: String,
        @Field("reservationId") reservationId: String
    ): Response<PaymentInfo>

    @GET("application/list")
    suspend fun getReservationHistory(
        @Header("Authorization") token: String,
        @Query("sortType") sortType: String,
        @Query("lastReservationId") lastReservationId: Int?
    ): Response<ReservationHistoryDto>

    @GET("list/{carbobId}")
    suspend fun getChargerReservation(
        @Header("Authorization") token: String,
        @Path("carbobId") carbobId: Int,
        @Query("lastReservationId") lastReservationId: Int?
    ): Response<ChargerReservationListDto>

    companion object {
        private const val BASE_URL = "${BuildConfig.BASE_URL}/v1/reservation/"

        fun create(): ReservationService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
                .build()
                .create(ReservationService::class.java)
        }
    }
}