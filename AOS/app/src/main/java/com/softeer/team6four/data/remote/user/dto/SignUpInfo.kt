package com.softeer.team6four.data.remote.user.dto

import kotlinx.serialization.Serializable

@Serializable
data class SignUpInfo(
    val email: String,
    val password: String,
    val nickname: String
)
