package com.softeer.team6four.data.remote.user.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserResult(
    val accessToken: String,
    val refreshToken: String,
    val nickname: String
)