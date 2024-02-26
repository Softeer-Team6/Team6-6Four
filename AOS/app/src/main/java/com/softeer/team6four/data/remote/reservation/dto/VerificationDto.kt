package com.softeer.team6four.data.remote.reservation.dto

import kotlinx.serialization.Serializable

@Serializable
data class VerificationDto(
    val data: VerificationInfo,
    val message: String,
    val statusCode: Int,
    val timestamp: String
)