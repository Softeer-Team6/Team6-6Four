package com.softeer.team6four.data.remote.reservation.dto

data class ChargerReservationList(
    val content: List<ChargerReservationInfo>,
    val hasNext: Boolean,
    val size: Int
)