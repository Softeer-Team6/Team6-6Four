package com.softeer.team6four.data.remote.charger.dto

data class MyChargerListDto(
    val data: MyChargerList,
    val message: String,
    val statusCode: Int,
    val timestamp: String
)