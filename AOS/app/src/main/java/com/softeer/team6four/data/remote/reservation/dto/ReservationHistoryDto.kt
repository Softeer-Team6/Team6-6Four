package com.softeer.team6four.data.remote.reservation.dto

data class ReservationHistoryDto(
    val data: ReservationInfoList,
    val message: String,
    val statusCode: Int,
    val timestamp: String
)