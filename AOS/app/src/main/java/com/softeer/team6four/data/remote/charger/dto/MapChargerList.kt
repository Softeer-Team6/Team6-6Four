package com.softeer.team6four.data.remote.charger.dto

import kotlinx.serialization.Serializable

@Serializable
data class MapChargerList(
    val content: List<MapChargerInfo>,
    val size: Int
)