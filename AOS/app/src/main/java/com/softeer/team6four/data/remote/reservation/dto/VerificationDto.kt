package com.softeer.team6four.data.remote.reservation.dto

data class VerificationDto(
    val data: VerificationInfo,
    val message: String,
    val statusCode: Int,
    val timestamp: String
)