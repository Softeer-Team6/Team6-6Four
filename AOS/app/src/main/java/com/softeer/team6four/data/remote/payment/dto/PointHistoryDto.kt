package com.softeer.team6four.data.remote.payment.dto

data class PointHistoryDto(
    val data: PointDetailList,
    val message: String,
    val statusCode: Int,
    val timestamp: String
)