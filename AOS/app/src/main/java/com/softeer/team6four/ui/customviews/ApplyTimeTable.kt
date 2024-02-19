package com.softeer.team6four.ui.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.softeer.team6four.R
import com.softeer.team6four.databinding.ApplyTimeTableBinding
import com.softeer.team6four.databinding.ApplyTimeTableRowBinding

class ApplyTimeTable(context: Context, attributes: AttributeSet) :
    ConstraintLayout(context, attributes) {
    private val binding = ApplyTimeTableBinding.inflate(LayoutInflater.from(context))
    private var reservationTimeList: List<Boolean> = emptyList()

    init {
        setTotalTimeText()
        //Test Code
        binding.secondRow.timeTableRowItemSixth.setBackgroundResource(R.drawable.background_apply_time_table_item_impossible)
        binding.thirdRow.timeTableRowItemFirst.setBackgroundResource(R.drawable.background_apply_time_table_item_impossible)
        addView(binding.root)
    }

    fun setList() {

    }

    private fun setTotalTimeText() {
        setRowTimeText(0, binding.firstRow)
        setRowTimeText(6, binding.secondRow)
        setRowTimeText(12, binding.thirdRow)
        setRowTimeText(18, binding.fourthRow)
    }

    private fun setRowTimeText(startTime: Int, applyTimeTableRowBinding: ApplyTimeTableRowBinding) {
        applyTimeTableRowBinding.tvTimeTableTextFirst.text = startTime.toString()
        applyTimeTableRowBinding.tvTimeTableTextSecond.text = startTime.plus(1).toString()
        applyTimeTableRowBinding.tvTimeTableTextThird.text = startTime.plus(2).toString()
        applyTimeTableRowBinding.tvTimeTableTextFourth.text = startTime.plus(3).toString()
        applyTimeTableRowBinding.tvTimeTableTextFifth.text = startTime.plus(4).toString()
        applyTimeTableRowBinding.tvTimeTableTextSixth.text = startTime.plus(5).toString()
        applyTimeTableRowBinding.tvTimeTableTextSeventh.text = startTime.plus(6).toString()
    }

}