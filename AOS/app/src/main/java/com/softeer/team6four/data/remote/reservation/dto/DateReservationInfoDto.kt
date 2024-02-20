package com.softeer.team6four.data.remote.reservation.dto

data class DateReservationInfoDto(
    val data: TimeTable,
    val message: String,
    val statusCode: Int,
    val timestamp: String
)