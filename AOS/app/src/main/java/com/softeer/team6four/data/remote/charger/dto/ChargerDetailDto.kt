package com.softeer.team6four.data.remote.charger.dto

data class ChargerDetailDto(
    val data: ChargerDetailInfo,
    val message: String,
    val statusCode: Int,
    val timestamp: String
)