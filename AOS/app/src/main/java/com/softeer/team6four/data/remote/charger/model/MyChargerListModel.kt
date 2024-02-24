package com.softeer.team6four.data.remote.charger.model

data class MyChargerListModel (
    val content: List<MyChargerSimpleInfoModel>,
    val hasNext: Boolean,
    val size: Int
)