package com.softeer.team6four.data.remote.geo.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GeoCodeResult(
    @Serializable val addresses: List<Addresses>? = null,
    val errorMessage: String? = null,
    @SerialName("meta") val meta: Meta? = null,
    val status: String
)