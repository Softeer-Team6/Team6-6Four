package com.softeer.team6four.data.remote.user.dto

import kotlinx.serialization.Serializable

@Serializable
data class EmailExistsResult(
    val emailExists: Boolean
)