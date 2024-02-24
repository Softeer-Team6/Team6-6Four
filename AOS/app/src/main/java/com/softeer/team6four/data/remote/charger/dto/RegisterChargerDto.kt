package com.softeer.team6four.data.remote.charger.dto

import kotlinx.serialization.Serializable

@Serializable
data class RegisterChargerDto(
    val data: Unit?,
    val message: String,
    val statusCode: Int,
    val timestamp: String
)