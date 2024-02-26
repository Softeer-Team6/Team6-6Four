package com.softeer.team6four.ui.mypage.register

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load

@BindingAdapter("imageUrl")
fun setImageUrl(imageView : ImageView, url : String?) {
    if (!url.isNullOrEmpty()) {
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        imageView.load(url)
    }
}