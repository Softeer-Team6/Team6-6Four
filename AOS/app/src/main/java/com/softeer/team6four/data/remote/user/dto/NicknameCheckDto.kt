package com.softeer.team6four.data.remote.user.dto

import kotlinx.serialization.Serializable

@Serializable
data class NicknameCheckDto(
    val data: NicknameExistsResult,
    val message: String,
    val statusCode: Int,
    val timestamp: String
)