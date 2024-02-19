package com.softeer.team6four.data.remote.charger.dto

data class UploadImageDto(
    val data: ImgUrl,
    val message: String,
    val statusCode: Int,
    val timestamp: String
)