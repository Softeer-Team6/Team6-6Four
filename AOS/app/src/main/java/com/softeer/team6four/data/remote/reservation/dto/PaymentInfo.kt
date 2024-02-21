package com.softeer.team6four.data.remote.reservation.dto

data class PaymentInfo(
    val address: String,
    val estimatedChargeAmount: String,
    val remainPoint: String,
    val rentalPoint: String,
    val reservationTime: String
)