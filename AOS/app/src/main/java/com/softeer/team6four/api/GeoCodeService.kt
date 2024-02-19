package com.softeer.team6four.api

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.softeer.team6four.data.remote.dto.GeoCodeResult
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface GeoCodeService {
    @GET("geocode")
    suspend fun coordinateResult(
        @Header("X-NCP-APIGW-API-KEY-ID") clientId: String,
        @Header("X-NCP-APIGW-API-KEY") clientSecret: String,
        @Query("query") address: String,
        @Query("count") count: Int
    ): Response<GeoCodeResult>

    companion object {
        private const val BASE_URL = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/"

        fun create(): GeoCodeService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
                .build()
                .create(GeoCodeService::class.java)
        }
    }
}