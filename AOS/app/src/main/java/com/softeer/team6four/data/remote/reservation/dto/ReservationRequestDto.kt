package com.softeer.team6four.data.remote.reservation.dto

import kotlinx.serialization.Serializable

@Serializable
data class ReservationRequestDto(
    val data: Unit?,
    val message: String,
    val statusCode: Int,
    val timestamp: String
)