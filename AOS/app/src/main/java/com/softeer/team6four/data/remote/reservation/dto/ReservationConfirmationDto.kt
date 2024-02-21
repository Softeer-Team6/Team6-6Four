package com.softeer.team6four.data.remote.reservation.dto

data class ReservationConfirmationDto(
    val data: ReservationState,
    val message: String,
    val statusCode: Int,
    val timestamp: String
)