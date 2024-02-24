package com.softeer.team6four.data.remote.reservation.dto

import kotlinx.serialization.Serializable

@Serializable
data class ReservationInfo(
    val address: String,
    val carbobImageUrl: String,
    val carbobNickname: String,
    val reservationId: Int,
    val reservationTime: ReservationTime,
    val reservationTimeStr: String,
    val stateType: String,
    val totalFee: Int
)