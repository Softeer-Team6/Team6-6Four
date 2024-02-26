package com.softeer.team6four.data.remote.reservation.model

data class PaymentInfoModel(
    val address: String = "",
    val estimatedChargeAmount: String = "",
    val remainPoint: String = "",
    val rentalPoint: String = "",
    val reservationTime: String = ""
)
