package com.softeer.team6four.data.remote.charger.model

data class RegistrationModel(
    val chargerNickname: String,
    val description: String,
    val feePer1kWh: Int,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val locationType: String,
    val chargeType: String,
    val speedType: String,
    val installType: String,
    val startTime: Int,
    val endTime: Int,
    val imageUrl : String
)
