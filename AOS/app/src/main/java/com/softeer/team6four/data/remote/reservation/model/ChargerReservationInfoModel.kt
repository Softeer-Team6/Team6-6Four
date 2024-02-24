package com.softeer.team6four.data.remote.reservation.model

data class ChargerReservationInfoModel (
    val address: String,
    val carbobNickname: String,
    val guestNickname: String,
    val rentalDate: String,
    val rentalTime: String,
    val reservationId: Int,
    val reservationTime: ReservationTimeModel,
    val totalFee: Int
)
