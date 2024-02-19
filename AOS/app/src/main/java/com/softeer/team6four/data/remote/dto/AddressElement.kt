package com.softeer.team6four.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class AddressElement(
    val code: String,
    val longName: String,
    val shortName: String,
    @Serializable val types: List<String>
)