package com.softeer.team6four.data.remote.fcm.dto

import kotlinx.serialization.Serializable

@Serializable
data class FcmResponseDto(
    val data: Unit?,
    val message: String,
    val statusCode: Int,
    val timestamp: String
)