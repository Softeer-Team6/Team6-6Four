package com.softeer.team6four.ui.apply

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import coil.load
import com.softeer.team6four.ui.customviews.ApplyTimeTable
import java.util.Calendar
import java.util.Date

@BindingAdapter("app:imageUrl")
fun setImageUrl(imageView: ImageView, url: String) {
    imageView.load(url)
}

@BindingAdapter("app:setDateText")
fun setDateText(textView: TextView, date: Date) {
    val calendar = Calendar.getInstance().apply { time = date }
    textView.text = "${calendar.get(Calendar.MONTH) + 1}월 ${calendar.get(Calendar.DATE)}일"
}

@BindingAdapter("app:setTimeList")
fun setTimeList(applyTimeTable: ApplyTimeTable, list : List<Boolean>) {
    applyTimeTable.setList(list)
}