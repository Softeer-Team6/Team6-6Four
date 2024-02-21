package com.softeer.team6four.data.remote.user.dto

data class LoginDto(
    val data: UserResult,
    val message: String,
    val statusCode: Int,
    val timestamp: String
)