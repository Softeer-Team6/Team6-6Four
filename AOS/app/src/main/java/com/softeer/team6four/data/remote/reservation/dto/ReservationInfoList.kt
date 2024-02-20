package com.softeer.team6four.data.remote.reservation.dto

data class ReservationInfoList(
    val content: List<ReservationInfo>,
    val hasNext: Boolean,
    val size: Int
)