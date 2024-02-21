package com.softeer.team6four.data.remote.geo.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("addresses")
data class Addresses(
    val addressElements: List<AddressElement>,
    val distance: Double,
    val englishAddress: String,
    val jibunAddress: String,
    val roadAddress: String,
    val x: String,
    val y: String
)