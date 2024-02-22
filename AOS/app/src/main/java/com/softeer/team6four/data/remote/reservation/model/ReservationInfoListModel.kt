package com.softeer.team6four.data.remote.reservation.model

data class ReservationInfoListModel (
    val content: List<ReservationInfoModel>,
    val hasNext: Boolean,
    val size: Int
)