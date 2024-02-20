package com.softeer.team6four.data.remote.reservation.dto

data class FulfillmentDto(
    val data: PaymentInfo,
    val message: String,
    val statusCode: Int,
    val timestamp: String
)