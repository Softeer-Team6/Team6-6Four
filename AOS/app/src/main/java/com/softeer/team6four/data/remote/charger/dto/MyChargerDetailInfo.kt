package com.softeer.team6four.data.remote.charger.dto

import kotlinx.serialization.Serializable

@Serializable
data class MyChargerDetailInfo(
    val address: String,
    val carbobId: Int,
    val carbobTotalIncome: Int,
    val chargerType: String,
    val description: String,
    val feePerHour: String,
    val imageUrl: String,
    val qrImageUrl: String,
    val installType: String,
    val nickname: String,
    val selfUseTime: String,
    val speedType: String
)