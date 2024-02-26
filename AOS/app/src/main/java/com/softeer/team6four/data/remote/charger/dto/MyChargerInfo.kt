package com.softeer.team6four.data.remote.charger.dto

import kotlinx.serialization.Serializable

@Serializable
data class MyChargerInfo(
    val carbobId: Int,
    val address: String,
    val imageUrl: String,
    val nickname: String,
    val reservationCount: Int
)