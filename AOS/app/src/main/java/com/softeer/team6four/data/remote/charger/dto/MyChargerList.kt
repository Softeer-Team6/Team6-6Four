package com.softeer.team6four.data.remote.charger.dto

import kotlinx.serialization.Serializable

@Serializable
data class MyChargerList(
    val content: List<MyChargerInfo>,
    val hasNext: Boolean,
    val size: Int
)