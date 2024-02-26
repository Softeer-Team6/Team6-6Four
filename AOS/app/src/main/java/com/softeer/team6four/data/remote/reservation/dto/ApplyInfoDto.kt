package com.softeer.team6four.data.remote.reservation.dto

import kotlinx.serialization.Serializable

@Serializable
data class ApplyInfoDto(
    val carbobId : Long,
    val startDateTime : String,
    val endDateTime : String
)
