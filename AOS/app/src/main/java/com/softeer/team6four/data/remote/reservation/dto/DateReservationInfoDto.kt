package com.softeer.team6four.data.remote.reservation.dto

import kotlinx.serialization.Serializable

@Serializable
data class DateReservationInfoDto(
    val data: TimeTable,
    val message: String,
    val statusCode: Int,
    val timestamp: String
)