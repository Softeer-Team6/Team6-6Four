package com.softeer.team6four.ui.home

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.softeer.team6four.data.remote.charger.model.BottomSheetChargerModel

@BindingAdapter("app:bottomSheetChargerlist")
fun setListItem(recyclerView: RecyclerView, list: List<BottomSheetChargerModel>) {
    (recyclerView.adapter as BottomSheetListAdapter).submitList(list.toList())
}