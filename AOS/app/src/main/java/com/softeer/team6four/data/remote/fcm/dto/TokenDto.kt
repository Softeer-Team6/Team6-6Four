package com.softeer.team6four.data.remote.fcm.dto

import kotlinx.serialization.Serializable

@Serializable
data class TokenDto(
    val fcmToken : String
)
