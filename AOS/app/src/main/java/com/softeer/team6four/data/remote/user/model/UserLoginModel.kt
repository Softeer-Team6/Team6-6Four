package com.softeer.team6four.data.remote.user.model

data class UserLoginModel(
    val accessToken: String,
    val refreshToken: String,
    val nickname: String
)
