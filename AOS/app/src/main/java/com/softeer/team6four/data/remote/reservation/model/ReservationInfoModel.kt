package com.softeer.team6four.data.remote.reservation.model

data class ReservationInfoModel (
    val address: String,
    val carbobImageUrl: String,
    val carbobNickname: String,
    val reservationId: Int,
    val reservationTime: ReservationTimeModel,
    val reservationTimeStr: String,
    val stateType: String,
    val totalFee: Int
)