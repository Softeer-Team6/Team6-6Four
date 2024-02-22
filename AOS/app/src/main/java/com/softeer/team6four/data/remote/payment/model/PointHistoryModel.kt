package com.softeer.team6four.data.remote.payment.model

data class PointHistoryModel (
    val content: List<PointHistoryDetailModel>,
    val hasNext: Boolean,
    val size: Int
)