package com.softeer.team6four.data.remote.charger.dto

import kotlinx.serialization.Serializable

@Serializable
data class BottomSheetChargerList(
    val content: List<BottomSheetChargerInfo>,
    val size: Int
)