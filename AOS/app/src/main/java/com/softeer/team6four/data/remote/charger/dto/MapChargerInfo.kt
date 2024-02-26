package com.softeer.team6four.data.remote.charger.dto

import kotlinx.serialization.Serializable

@Serializable
data class MapChargerInfo(
    val carbobId: Long,
    val feePerHour: String,
    val latitude: Double,
    val longitude: Double
)