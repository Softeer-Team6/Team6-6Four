package com.softeer.team6four.api

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.softeer.team6four.BuildConfig
import com.softeer.team6four.data.Resource
import com.softeer.team6four.data.remote.user.dto.EmailCheckDto
import com.softeer.team6four.data.remote.user.dto.LoginDto
import com.softeer.team6four.data.remote.user.dto.NicknameCheckDto
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UserService {
    @POST("auth/signup")
    suspend fun requestSignUp(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("nickname") nickname: String
    ): Resource<Unit>

    @GET("auth/email/check")
    suspend fun checkEmail(
        @Query("email") email: String
    ): Resource<EmailCheckDto>

    @GET("auth/nickname/check")
    suspend fun checkNickname(
        @Query("nickname") nickname: String
    ): Resource<NicknameCheckDto>

    @POST("auth/signin")
    suspend fun requestLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): Resource<LoginDto>

    companion object {
        private const val BASE_URL = "${BuildConfig.BASE_URL}/v1/user/"

        fun create(): UserService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
                .build()
                .create(UserService::class.java)
        }
    }
}