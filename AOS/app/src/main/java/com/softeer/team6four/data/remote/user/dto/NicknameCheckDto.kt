package com.softeer.team6four.data.remote.user.dto

data class NicknameCheckDto(
    val data: NicknameExistsResult,
    val message: String,
    val statusCode: Int,
    val timestamp: String
)