package com.softeer.team6four.data.remote.payment.model

data class PointHistoryDetailModel (
    val amount: String,
    val createdDate: String,
    val paymentId: Int,
    val paymentType: String,
    val pointTitle: String,
    val targetId: Int
)