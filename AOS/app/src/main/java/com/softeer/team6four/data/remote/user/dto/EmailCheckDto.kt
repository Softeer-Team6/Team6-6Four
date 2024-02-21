package com.softeer.team6four.data.remote.user.dto

data class EmailCheckDto(
    val data: EmailExistsResult,
    val message: String,
    val statusCode: Int,
    val timestamp: String
)