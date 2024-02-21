package com.softeer.team6four.data.remote.reservation.dto

data class ChargerReservationListDto(
    val data: ChargerReservationList,
    val message: String,
    val statusCode: Int,
    val timestamp: String
)