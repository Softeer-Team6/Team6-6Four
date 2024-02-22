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

    init {
        setTotalTimeText()
        addView(binding.root)
    }

    fun setList(list: List<Boolean>) {
        setTotalBackground(list)
    }

    private fun setTotalBackground(list: List<Boolean>) {
        setRowBackground(binding.firstRow, list.subList(0, 6))
        setRowBackground(binding.secondRow, list.subList(6, 12))
        setRowBackground(binding.thirdRow, list.subList(12, 18))
        setRowBackground(binding.fourthRow, list.subList(18, 24))
    }

    private fun setRowBackground(
        applyTimeTableRowBinding: ApplyTimeTableRowBinding,
        list: List<Boolean>
    ) {
        applyTimeTableRowBinding.timeTableRowItemFirst.setBackgroundResource(
            if (!list[0]) R.drawable.background_apply_time_table_item_possible
            else R.drawable.background_apply_time_table_item_impossible
        )
        applyTimeTableRowBinding.timeTableRowItemSecond.setBackgroundResource(
            if (!list[1]) R.drawable.background_apply_time_table_item_possible
            else R.drawable.background_apply_time_table_item_impossible
        )
        applyTimeTableRowBinding.timeTableRowItemThird.setBackgroundResource(
            if (!list[2]) R.drawable.background_apply_time_table_item_possible
            else R.drawable.background_apply_time_table_item_impossible
        )
        applyTimeTableRowBinding.timeTableRowItemFourth.setBackgroundResource(
            if (!list[3]) R.drawable.background_apply_time_table_item_possible
            else R.drawable.background_apply_time_table_item_impossible
        )
        applyTimeTableRowBinding.timeTableRowItemFifth.setBackgroundResource(
            if (!list[4]) R.drawable.background_apply_time_table_item_possible
            else R.drawable.background_apply_time_table_item_impossible
        )
        applyTimeTableRowBinding.timeTableRowItemSixth.setBackgroundResource(
            if (!list[5]) R.drawable.background_apply_time_table_item_possible
            else R.drawable.background_apply_time_table_item_impossible
        )

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