package com.softeer.team6four.data.remote.reservation.dto

import kotlinx.serialization.Serializable

@Serializable
data class VerificationInfo(
    val isVerified: Boolean,
    val reservationId: Long
)
