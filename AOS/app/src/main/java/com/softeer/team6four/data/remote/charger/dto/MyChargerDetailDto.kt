package com.softeer.team6four.data.remote.charger.dto

import kotlinx.serialization.Serializable

@Serializable
data class MyChargerDetailDto(
    val data: MyChargerDetailInfo,
    val message: String,
    val statusCode: Int,
    val timestamp: String
)