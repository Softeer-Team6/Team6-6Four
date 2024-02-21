package com.softeer.team6four.data.remote.reservation.dto

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