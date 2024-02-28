package com.softeer.team6four.data.remote.payment.dto

import kotlinx.serialization.Serializable

@Serializable
data class PointChargeDto(
    val data: Unit?,
    val message: String,
    val statusCode: Int,
    val timestamp: String
)