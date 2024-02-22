package com.softeer.team6four.data.remote.charger.dto

import kotlinx.serialization.Serializable
@Serializable
data class BottomSheetChargerInfo(
    val address: String,
    val carbobId: Int,
    val feePerHour: String,
    val nickname: String,
    val speedType: String
)