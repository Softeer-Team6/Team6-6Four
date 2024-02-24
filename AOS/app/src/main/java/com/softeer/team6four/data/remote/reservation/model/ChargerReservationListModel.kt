package com.softeer.team6four.data.remote.reservation.model

data class ChargerReservationListModel (
    val content: List<ChargerReservationInfoModel>,
    val hasNext: Boolean,
    val size: Int
)