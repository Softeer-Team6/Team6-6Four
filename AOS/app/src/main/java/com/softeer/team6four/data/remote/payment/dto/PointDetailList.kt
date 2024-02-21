package com.softeer.team6four.data.remote.payment.dto

data class PointDetailList(
    val content: List<PointDetail>,
    val hasNext: Boolean,
    val size: Int
)