package com.softeer.team6four.data.remote.charger.dto

import kotlinx.serialization.Serializable

@Serializable
data class ChargerRegistrationInfo(
    val carbobNickname: String,
    val description: String,
    val feePer1kwh: Int,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val locationType: String,
    val chargeType: String,
    val speedType: String,
    val installType: String,
    val startDateTime: String,
    val endDateTime: String,
    val carbobImgUrl: String
)
