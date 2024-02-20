package com.softeer.team6four.data.remote.payment.dto

data class PointChargeDto(
    val data: Point,
    val message: String,
    val statusCode: Int,
    val timestamp: String
)