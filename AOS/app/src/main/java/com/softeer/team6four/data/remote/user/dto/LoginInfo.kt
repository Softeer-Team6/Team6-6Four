package com.softeer.team6four.data.remote.user.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginInfo(
    val email: String,
    val password: String
)
