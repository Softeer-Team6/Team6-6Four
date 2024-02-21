package com.softeer.team6four.api

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.softeer.team6four.BuildConfig
import com.softeer.team6four.data.remote.fcm.dto.FcmResponseDto
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface FcmService {
    @POST("my/token")
    suspend fun postToken(
        @Header("Authorization") authorization: String,
        @Body fcmToken: String
    ): Response<FcmResponseDto>


    companion object {
        private const val BASE_URL = "${BuildConfig.BASE_URL}/v1/fcm/"

        fun create(): FcmService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
                .build()
                .create(FcmService::class.java)
        }
    }
}