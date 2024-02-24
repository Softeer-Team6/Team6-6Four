package com.softeer.team6four.api

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.softeer.team6four.BuildConfig
import com.softeer.team6four.data.remote.charger.dto.BottomSheetChargerListDto
import com.softeer.team6four.data.remote.charger.dto.ChargerDetailDto
import com.softeer.team6four.data.remote.charger.dto.ChargerRegistrationInfo
import com.softeer.team6four.data.remote.charger.dto.MapChargerListDto
import com.softeer.team6four.data.remote.charger.dto.MyChargerDetailDto
import com.softeer.team6four.data.remote.charger.dto.MyChargerListDto
import com.softeer.team6four.data.remote.charger.dto.RegisterChargerDto
import com.softeer.team6four.data.remote.charger.dto.UploadImageDto
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ChargerService {
    @GET("main/detail/{carbobId}")
    suspend fun getChargerDetail(
        @Header("Authorization") token: String,
        @Path("carbobId") carbobId: Long,
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double
    ): Response<ChargerDetailDto>

    @GET("main")
    suspend fun getMapChargerListDto(
        @Header("Authorization") token: String,
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
    ): Response<MapChargerListDto>

    @GET("main/footer")
    suspend fun getBottomSheetChargerListDto(
        @Header("Authorization") token: String,
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("sortType") sortType: String? = null
    ): Response<BottomSheetChargerListDto>

    @POST("registration")
    suspend fun registerCharger(
        @Header("Authorization") token: String,
        @Body chargerRegistrationInfo: ChargerRegistrationInfo
    ): Response<RegisterChargerDto>

    @Multipart
    @POST("image")
    suspend fun uploadImage(
        @Header("Authorization") token: String,
        @Part image: MultipartBody.Part
    ): Response<UploadImageDto>

    @GET("my")
    suspend fun getMyChargerList(
        @Header("Authorization") token: String,
        @Query("sortType") sortType: String,
        @Query("lastCarbobId") lastCarbobId: Int?,
        @Query("lastReservationCount") lastReservationCount: Int?
    ): Response<MyChargerListDto>

    @GET("detail/{carbobId}")
    suspend fun getMyChargerDetail(
        @Header("Authorization") token: String,
        @Query("carbobId") carbobId: Int
    ): Response<MyChargerDetailDto>

    companion object {
        private const val BASE_URL = "${BuildConfig.BASE_URL}/v1/carbob/"

        fun create(): ChargerService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
                .build()
                .create(ChargerService::class.java)
        }
    }
}