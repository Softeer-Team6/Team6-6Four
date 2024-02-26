package com.softeer.team6four.data.remote.charger.model

data class MapChargerModel(
    val chargerId: Long,
    val feePerHour: String,
    val latitude: Double,
    val longitude: Double
)