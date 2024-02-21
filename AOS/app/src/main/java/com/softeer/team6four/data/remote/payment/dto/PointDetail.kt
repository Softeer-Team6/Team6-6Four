package com.softeer.team6four.data.remote.payment.dto

data class PointDetail(
    val amount: Int,
    val createdDate: String,
    val paymentId: Int,
    val paymentType: String,
    val pointTitle: String,
    val targetId: Int
)