package com.softeer.team6four.data.remote.reservation.dto

import kotlinx.serialization.Serializable

@Serializable
data class ChargerReservationList(
    val content: List<ChargerReservationInfo>,
    val hasNext: Boolean,
    val size: Int
)