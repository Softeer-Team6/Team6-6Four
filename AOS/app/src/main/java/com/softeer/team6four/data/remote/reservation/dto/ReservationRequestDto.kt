package com.softeer.team6four.data.remote.reservation.dto

data class ReservationRequestDto(
    val data: Any,
    val message: String,
    val statusCode: Int,
    val timestamp: String
)