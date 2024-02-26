package com.softeer.team6four.data.remote.charger.model

data class ChargerDetailModel(
    val address: String,
    val chargerId: Long,
    val chargerType: String,
    val description: String,
    val distance: Double,
    val feePerHour: String,
    val imageUrl: String,
    val installType: String,
    val nickname: String,
    val speedType: String
)
