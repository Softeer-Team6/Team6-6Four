package com.softeer.team6four.data.remote.reservation.dto

import kotlinx.serialization.Serializable

@Serializable
data class ChargerReservationInfo(
    val address: String,
    val carbobNickname: String,
    val guestNickname: String,
    val rentalDate: String,
    val rentalTime: String,
    val reservationId: Int,
    val reservationTime: ReservationTime,
    val totalFee: Int
)