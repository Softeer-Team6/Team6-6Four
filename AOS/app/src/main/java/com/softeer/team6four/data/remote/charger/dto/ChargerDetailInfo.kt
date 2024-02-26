package com.softeer.team6four.data.remote.charger.dto

import kotlinx.serialization.Serializable

@Serializable
data class ChargerDetailInfo(
    val address: String,
    val carbobId: Long,
    val chargerType: String,
    val description: String,
    val distance: Double,
    val feePerHour: String,
    val imageUrl: String,
    val installType: String,
    val nickname: String,
    val speedType: String
)